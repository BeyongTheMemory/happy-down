package com.top.mini.happy.down.exception;

import lombok.Data;

/**
 * @author xugang on 18/1/26.
 */
@Data
public class HappyDownException extends RuntimeException {
    private int code;
    private String msg;

    public HappyDownException(int code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }
}
