package com.top.mini.happy.down.http.interceptor;


import com.alibaba.fastjson.JSON;
import com.top.mini.happy.down.http.HttpClient;
import com.top.mini.happy.down.http.HttpClientImpl;
import com.top.mini.happy.down.http.annotion.HttpParam;
import com.top.mini.happy.down.http.annotion.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xugang  on 17/9/8.
 */
@Slf4j
@Aspect
@Component
public class HttpAspect {

    @Around(value = "within(com.top.mini.happy.down..*) && @annotation(httpRequest)")
    public Object cache(ProceedingJoinPoint joinPoint, HttpRequest httpRequest) throws Throwable {
        //组装请求参数
        HttpClient.Params params = getHttpParam(joinPoint);
        //发送请求
        String result = getHttpResult(httpRequest, params);
        //转换结果返回
        return parseResult(result,((MethodSignature) joinPoint.getSignature()).getMethod().getReturnType());
    }


    private Object parseResult(String result, Class targetClass) {
        return JSON.parseObject(result,targetClass);
    }

    private HttpClient.Params getHttpParam(ProceedingJoinPoint joinPoint) {
        Parameter[] parameters = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameters();
        Object[] args = joinPoint.getArgs();
        Map<String, Object> params = new HashMap<>(parameters.length);
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            HttpParam httpParam = parameter.getAnnotation(HttpParam.class);
            String name = httpParam != null ? httpParam.value() : parameter.getName();
            if (args[i] != null) {
                params.put(name, JSON.toJSONString(args[i]));
            }
        }

        return HttpClient.Params.custom().
                add(params).
                build();
    }


    private String getHttpResult(HttpRequest httpRequest, HttpClient.Params params) {
        switch (httpRequest.method()) {
            case GET:
                return HttpClientImpl.getInstance().get(httpRequest.url(), params);
            case POST:
                return HttpClientImpl.getInstance().post(httpRequest.url(), params);
            case DELETE:
                return HttpClientImpl.getInstance().delete(httpRequest.url(), params);
            case PUT:
                return HttpClientImpl.getInstance().put(httpRequest.url(), params);
            default:
                return null;
        }
    }

}
