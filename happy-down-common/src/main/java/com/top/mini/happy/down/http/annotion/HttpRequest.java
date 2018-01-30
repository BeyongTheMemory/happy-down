package com.top.mini.happy.down.http.annotion;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author xugang on 18/1/29.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpRequest {
    /**
     * http请求方法
     * 默认get
     * @return
     */
    HttpRequestMethod method() default HttpRequestMethod.GET;

    /**
     * http请求地址
     */
    String url();
}
