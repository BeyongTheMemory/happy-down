package com.top.mini.happy.down.http.annotion;

import java.lang.annotation.*;

/**
 * @author xugang on 18/1/9.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface HttpParam {

    String value();

    boolean ignore() default false;
}
