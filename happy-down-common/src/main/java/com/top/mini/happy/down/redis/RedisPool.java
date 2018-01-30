package com.top.mini.happy.down.redis;

import org.springframework.stereotype.Repository;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;

import javax.annotation.Resource;

/**
 * @author nizhicheng
 * @date 16/3/22
 */
@Repository
public class RedisPool {

    @Resource(name = "jedis.pool")
    private JedisPool pool;

    public Jedis getConnection() {
        Jedis con;
        try {
            con = pool.getResource();
        } catch (JedisConnectionException e) {
            throw e;
        }
        return con;
    }

    public void closeConnection(Jedis con) {
        try {
            if (con != null) {
                con.close();
            }
        } catch (JedisException e) {
            throw e;
        }
    }
}
