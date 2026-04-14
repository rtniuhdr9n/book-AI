package com.bookai.exception;

import com.bookai.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public Result handleRuntimeException(RuntimeException e) {
        log.error("运行时异常:", e);
        return Result.error(e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public Result handleValidationException(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder message = new StringBuilder();
        bindingResult.getFieldErrors().forEach(error -> {
            message.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
        });
        return Result.error(message.toString());
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error("系统异常:", e);
        return Result.error("系统繁忙，请稍后重试");
    }
}
