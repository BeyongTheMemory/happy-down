package com.top.mini.happy.down.response;

import lombok.Data;

/**
 * @author xugang on 18/1/26.
 */
@Data
public class Result<T>{
    private T data;
    private boolean success = true;
    private int code = 200;
    private String message;
}
