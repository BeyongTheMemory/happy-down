package com.top.mini.happy.down.exception;

/**
 * @author xugang on 18/1/26.
 */

public enum ErrorCode {

    INVALID_PARAM(133900200, "入参不符合规则"),
    INVALID_METHOD(133900201, "不支持的请求类型"),
    SYSTEM_ERROR_CODE(-1, "系统错误");

    private int code;
    private String msg;

    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static ErrorCode getByCode(int code) {
        for (ErrorCode codeEnum : ErrorCode.values()) {
            if (codeEnum.getCode() == code) {
                return codeEnum;
            }
        }
        return SYSTEM_ERROR_CODE;
    }


    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
