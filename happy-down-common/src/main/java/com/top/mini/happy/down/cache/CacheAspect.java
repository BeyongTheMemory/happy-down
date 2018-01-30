package com.top.mini.happy.down.cache;

import com.alibaba.fastjson.JSON;
import com.top.mini.happy.down.cache.anotion.DelCache;
import com.top.mini.happy.down.cache.anotion.NeedCache;
import com.top.mini.happy.down.redis.RedisKeyEnum;
import com.top.mini.happy.down.redis.RedisOperate;
import com.top.mini.happy.down.util.SpelUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author xugang  on 17/9/8.
 */
@Slf4j
@Aspect
@Component
public class CacheAspect {

    @Resource
    private RedisOperate redis;

    private static final String NULL = "null";

    @Around(value = "within(com.top.mini.happy.down..*) && @annotation(needCache)")
    public Object cache(ProceedingJoinPoint joinPoint, NeedCache needCache) throws Throwable {
        try {
            String redisResult = redis.getStringByKey(getKey(joinPoint, needCache));
            if (!StringUtils.isEmpty(redisResult)) {
                if (!redisResult.equals(NULL)) {
                    return JSON.parseObject(redisResult, ((MethodSignature) joinPoint.getSignature()).getMethod().getReturnType());
                } else {
                    return null;
                }
            }
        }catch (Exception e){
            //所有异常不抛出
            log.warn("cache error:redis.get:",e);
        }

        //真实方法调用
        Object result = joinPoint.proceed(joinPoint.getArgs());
        try {
            if (result != null) {
                if(needCache.redisEnum().getTTL() > 0){
                    redis.set(getKey(joinPoint, needCache), JSON.toJSONString(result), needCache.redisEnum().getTTL());
                }else {
                    redis.set(getKey(joinPoint, needCache), JSON.toJSONString(result));
                }
            } else {
                //缓存null值防击穿
                if(needCache.redisEnum().getTTL() > 0){
                    redis.set(getKey(joinPoint, needCache), NULL,needCache.redisEnum().getTTL());
                }else {
                    redis.set(getKey(joinPoint, needCache), NULL);
                }
            }
        }catch (Exception e){
            //所有异常不抛出
            log.error("cache error:redis.set:",e);
        }
        return result;
    }

    @AfterReturning(value = "within(com.youzan.app.engine..*) && @annotation(delCache)")
    public void delCache(JoinPoint joinPoint, DelCache delCache) throws Throwable {
        redis.del(getKey(joinPoint, delCache));
    }

    public String getKey(JoinPoint joinPoint, Annotation annotation) {
        Signature signature = joinPoint.getSignature();
        Method method = ((MethodSignature) signature).getMethod();
        Object[] args = joinPoint.getArgs();
        //缓存获取
        String key;
        String values = null;
        RedisKeyEnum redisEnum = null;
        if (annotation instanceof NeedCache) {
            values = ((NeedCache) annotation).value();
            redisEnum = ((NeedCache) annotation).redisEnum();
        } else if (annotation instanceof DelCache) {
            values = ((DelCache) annotation).value();
            redisEnum = ((DelCache) annotation).redisEnum();
        } else {
            return null;
        }
        if (!StringUtils.isEmpty(values)) {
            String[] valueStrings = values.split(",");
            for (int i = 0; i < valueStrings.length; i++) {
                String value = valueStrings[i];
                if (value.startsWith("#")) {
                    valueStrings[i] = SpelUtil.getKey(method, args, value);
                }
            }
            key = String.format(redisEnum.getKey(), valueStrings);
        } else {
            key = redisEnum.getKey();
        }
        return key;
    }

}
