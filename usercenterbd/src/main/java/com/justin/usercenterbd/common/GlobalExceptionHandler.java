package com.justin.usercenterbd.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {
//        log.error("businessException: " + e.getMessage(), e);
        return ResultUitls.error(e.getCode(), e.getMessage(), e.getDescription());
    }

    //https://github.com/liyupi

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
//        log.error("runtimeException", e);
        return ResultUitls.error(ErrorCode.SYSTEM_ERROR, e.getMessage(), "");
    }
}
