package com.myzhihu.service;

import com.myzhihu.util.JedisAdapter;
import com.myzhihu.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
//点赞
public class LikeService {
    @Autowired
    JedisAdapter jedisAdapter;

    //点赞，set中的LikeKey加入userId,disLikeKey删除掉userId
    public long like(int userId,int entityId,int entityType){
        String likeKey = RedisKeyUtil.getLikeKey(entityId,entityType);
        jedisAdapter.sadd(likeKey,String.valueOf(userId));

        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId,entityType);
        jedisAdapter.srem(disLikeKey,String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }

    //点踩，set中的DisLikeKey加入userId,LikeKey删除掉userId
    public long disLike(int userId,int entityId,int entityType){
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId,entityType);
        jedisAdapter.sadd(disLikeKey,String.valueOf(userId));

        String likeKey = RedisKeyUtil.getLikeKey(entityId,entityType);
        jedisAdapter.srem(likeKey,String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }

    //点赞之后图标发亮，我们需要获取用户点赞的状态
    //赞返回1 ，踩返回-1 ，不赞不踩返回0
    public int getLikeStatus(int userId,int entityId,int entityType){
        String likeKey = RedisKeyUtil.getLikeKey(entityId,entityType);
        if(jedisAdapter.sismember(likeKey,String.valueOf(userId))){
            return 1;
        }

        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityId,entityType);
        return jedisAdapter.sismember(disLikeKey,String.valueOf(userId))? -1 : 0;
    }

    //多少人赞
    public long getLikeCount(int entityId,int entityType){
        String likeKey = RedisKeyUtil.getLikeKey(entityId,entityType);
        return jedisAdapter.scard(likeKey);
    }
}
