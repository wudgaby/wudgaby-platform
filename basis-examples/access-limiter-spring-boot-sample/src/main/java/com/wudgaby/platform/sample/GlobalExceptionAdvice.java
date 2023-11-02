package com.wudgaby.platform.sample;

import com.wudgaby.starter.access.limit.AccessLimitException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/11/2 0002 14:42
 * @desc :
 */
@Slf4j
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@RestControllerAdvice
public class GlobalExceptionAdvice {
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String ex(Exception ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(AccessLimitException.class)
    @ResponseBody
    public String accessEx(AccessLimitException ex) {
        return "AccessLimitException: " + ex.getMessage();
    }
}
