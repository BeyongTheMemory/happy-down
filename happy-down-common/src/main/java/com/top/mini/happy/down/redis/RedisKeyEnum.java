package com.top.mini.happy.down.redis;

/**
 * @author xugang on 18/1/29.
 */

public enum RedisKeyEnum {
    /**
     * 排行榜
     * 无过期时间
     */
    SCORE_RANK("SCORE_RANK",-1),
    /**
     * 用户基本信息
     */
    USER("USER:%s",60 * 60 * 24),
    ;
    private String key;
    private int ttl;

    RedisKeyEnum(String key, int ttl) {
        this.key = key;
        this.ttl = ttl;
    }

    public String getKey() {
        return key;
    }

    public int getTTL() {
        return ttl;
    }
}
