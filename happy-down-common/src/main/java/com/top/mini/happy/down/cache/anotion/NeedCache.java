package com.top.mini.happy.down.cache.anotion;


import com.top.mini.happy.down.redis.RedisKeyEnum;

import java.lang.annotation.*;

/**
 * Created by xugang on 17/9/8.
 * 标示需要缓存的方法
 * 仅在方法不为void时有效
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface NeedCache {

    /**
     * 缓存健值模板
     */
    RedisKeyEnum redisEnum();

    /**
     * 支持SpEL表达式，替换模板中的值
     * 多个值使用 , 分开
     */
    String value() default "";

}
