package com.top.mini.happy.down.handle;

import com.top.mini.happy.down.exception.ErrorCode;
import com.top.mini.happy.down.exception.HappyDownException;
import com.top.mini.happy.down.response.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author xugang
 * @date 17/9/5
 * control层统一异常拦截
 */
@ControllerAdvice
public class ExceptionAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public Result<Object> handleException(Throwable ex){
        Result<Object> result = new Result<>();
        if(ex instanceof HappyDownException){
            result.setCode(((HappyDownException) ex).getCode());
            result.setMessage(ex.getMessage());
        } else if (ex instanceof BindException) {
            result.setCode(ErrorCode.INVALID_PARAM.getCode());
            result.setMessage(((BindException) ex).getFieldError().getDefaultMessage());
        } else if(ex instanceof MethodArgumentNotValidException) {
            result.setCode(ErrorCode.INVALID_PARAM.getCode());
            result.setMessage(((MethodArgumentNotValidException) ex).getBindingResult().getFieldError().getDefaultMessage());
        } else if(ex instanceof HttpRequestMethodNotSupportedException){
            result.setCode(ErrorCode.INVALID_METHOD.getCode());
            result.setMessage(ErrorCode.INVALID_METHOD.getMsg());
        } else {
            result.setCode(-1);
            result.setMessage("系统错误,请稍后再试");
        }
        if(ex instanceof HappyDownException){
            logger.warn("system error:", ex);
        }
        else {
            logger.error("Exception occurs.", ex);
        }
        return result;
    }
}
