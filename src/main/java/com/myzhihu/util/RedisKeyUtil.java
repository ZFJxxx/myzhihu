package com.myzhihu.util;

/*
 *生成redis的key的工具
 */
public class RedisKeyUtil {
        private static String SPLIT = ":";
        //赞与踩
        private static String BIZ_LIKE = "LIKE";
        private static String BIZ_DISLIKE = "DISLIKE";
        private static String BIZ_EVENTQUEUE = "EVENT_QUEUE";
        //粉丝 我关注A，我就是A的粉丝
        private static String BIZ_FOLLOWER = "FOLLOWER";
        //关注对象，我关注了A，A就是我的关注对象
        private static String BIZ_FOLLOWEE = "FOLLOWEE";

        public static String getLikeKey(int entityType, int entityId) {
            return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
        }

        public static String getDisLikeKey(int entityType, int entityId) {
            return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
        }

        public static String getEventQueueKey() {
            return BIZ_EVENTQUEUE;
        }

        public static String getFollowerKey(int entityType,int entityId){
            return BIZ_FOLLOWER + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
        }

        public static String getFolloweeKey(int userId,int entityType){
            return BIZ_FOLLOWEE + SPLIT + String.valueOf(userId) + SPLIT + String.valueOf(entityType);
        }
    }

