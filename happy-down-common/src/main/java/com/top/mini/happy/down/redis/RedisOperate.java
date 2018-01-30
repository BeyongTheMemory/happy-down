package com.top.mini.happy.down.redis;

import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */


public interface RedisOperate {

    void set(String key, String value);

    void set(String key, String value, int second);

    void setnx(String key, String value, int second);

    void createCountKey(String key, int value);

    void del(String key);

    String getStringByKey(String key);

    List<String> getStringByKeys(List<String> keys);

    /**
     * 计数
     */
    void keyDecrement(String key, int step);

    Long keyIncrement(String key, int step);

    /**
     * set
     */
    void setAdd(String key, String value);

    void setDel(String key, String value);

    Set<String> getSetMember(String key);

    Long getSetSize(String key);

    /**
     * zset
     */
    void zsetAdd(String key, long score, String value);

    void zsetDel(String key, String value);

    Long getZsetSize(String key);

    Set<String> getZsetByscoreByPage(String key, String min, String max, int offset, int count);

    Set<String> getZsetByScore(String key, String min, String max);

    Double zscore(String key,String member);

    /**
     * 分数倒叙输出
     * @param key 集合key
     * @param begin 起始下标，从0开始
     * @param end 终止下标
     * @return 带分数的结果
     */
    Set<Tuple> zrevrangeWithScores(String key,long begin,long end);

    Long zsetRemrangebyScore(String key, String min, String max);

    /**
     * list
     */
    void listAdd(String key, String value);

    List<String> getListAll(String key, int start, int stop);

    void listRangeDel(String key, int start, int stop);

    Long listDelByValue(String key, long count, String value);

    /**
     * hash
     */
    String hmset(String key, Map<String, String> map);

    Long hincrby(String key, String field, long step);

    String hget(String key, String field);

    Map<String, String> hgetall(String key);

    /**
     * 事务处理
     */
    List<Object> getZsetByScoreAndRemove(String key, String min, String max);

    /**
     * 设置key过期时间
     */
    Long setExpire(String key, int seconds);

    void hdel(String key, String field);


}
