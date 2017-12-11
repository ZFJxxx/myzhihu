package com.myzhihu.service;

import com.myzhihu.util.JedisAdapter;
import com.myzhihu.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class FollowService {
    @Autowired
    JedisAdapter jedisAdapter;


    //把关注对象放到关注对象的列表中，把自己放到关注对象的粉丝列表中
    public boolean follow(int userId,int entityType,int entityId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);
        //关注时间
        Date date = new Date();
        Jedis jedis = jedisAdapter.getJedis();
        //开启事务
        Transaction tx = jedisAdapter.multi(jedis);
        //在我关注的实体的列表中加入我的userId
        tx.zadd(followerKey,date.getTime(),String.valueOf(userId));
        //我关注的对象中加入被关注的实体
        tx.zadd(followeeKey,date.getTime(),String.valueOf(entityId));
        List<Object> ret = jedisAdapter.exec(tx,jedis);
        return ret.size() == 2 && (long) ret.get(0)>0 && (long) ret.get(1) > 0;
    }

    //取消关注，列表中相互删除
    public boolean unfollow(int userId,int entityType,int entityId){
        String followerKey = RedisKeyUtil.getFollowerKey(entityType,entityId);
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId,entityType);
        //关注时间
        Date date = new Date();
        Jedis jedis = jedisAdapter.getJedis();
        //开启事务
        Transaction tx = jedisAdapter.multi(jedis);
        tx.zrem(followerKey,String.valueOf(userId));
        tx.zrem(followeeKey,String.valueOf(entityId));
        List<Object> ret = jedisAdapter.exec(tx,jedis);
        return ret.size() == 2 && (long) ret.get(0)>0 && (long) ret.get(1) > 0;
    }

    private List<Integer> getIdsFromSet(Set<String> idset) {
        List<Integer> ids = new ArrayList<>();
        for (String str : idset) {
            ids.add(Integer.parseInt(str));
        }
        return ids;
    }

    //实体有哪些关注者
    public List<Integer> getFollowers(int entityType, int entityId, int count) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return getIdsFromSet(jedisAdapter.zrevrange(followerKey, 0, count));
    }

    public List<Integer> getFollowers(int entityType, int entityId, int offset, int count) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return getIdsFromSet(jedisAdapter.zrevrange(followerKey, offset, offset+count));
    }

    //我关注了那些实体
    public List<Integer> getFollowees(int userId, int entityType, int count) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return getIdsFromSet(jedisAdapter.zrevrange(followeeKey, 0, count));
    }

    public List<Integer> getFollowees(int userId, int entityType, int offset, int count) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return getIdsFromSet(jedisAdapter.zrevrange(followeeKey, offset, offset+count));
    }

    //实体有多少粉丝
    public long getFollowerCount(int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return jedisAdapter.zcard(followerKey);
    }


    //我关注了多少实体
    public long getFolloweeCount(int userId, int entityType) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return jedisAdapter.zcard(followeeKey);
    }

    //是否关注
    public boolean isFollower(int userId, int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return jedisAdapter.zscore(followerKey, String.valueOf(userId)) != null;
    }
}
