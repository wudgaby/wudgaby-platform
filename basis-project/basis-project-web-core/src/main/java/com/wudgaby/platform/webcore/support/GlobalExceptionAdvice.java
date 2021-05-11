package com.wudgaby.platform.webcore.support;

import com.wudgaby.platform.core.exception.BusinessException;
import com.wudgaby.platform.core.exception.ValidatorFormException;
import com.wudgaby.platform.core.model.dto.ValidationErrorDTO;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.core.result.enums.SystemResultCode;
import com.wudgaby.platform.core.support.FormValidator;
import com.wudgaby.platform.limiter.core.LimitException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.Iterator;

/**
 * @ClassName : GlobalExceptionAdvice
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2018/9/24/024 2:28
 * @Desc :   统一异常处理
 */
@Slf4j
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@RestControllerAdvice
public class GlobalExceptionAdvice {
    @Autowired private FormValidator formValidator;

    @Value("${spring.application.name:UNKNOWN_APP_NAME}")
    private String appName;

    /**
     * 404时,返回json结构数据
     * @param ex  webflux不支持.
     * @return
     */
    /*@ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResult<String> notFountHandler(NoHandlerFoundException ex){
        String message = String.format("[%s] %s 未找到该接口!", ex.getHttpMethod(), ex.getRequestURL());
        log.error("{}", message);
        return ApiResult.<String>createInstance(SystemResultCode.NOT_FOUND).data(message);
    }*/

    /**
     * 请求方法不支持
     * @param ex
     * @return
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiResult<String> httpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error(ex.getMessage(), ex);
        return ApiResult.failure(SystemResultCode.PARAM_IS_INVALID);
    }

    /**
     * 缺少请求数据
     * @param ex
     * @return
     */
    @ExceptionHandler(ServletRequestBindingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiResult handleMethodArgumentNotValidException(ServletRequestBindingException ex) {
        log.error(ex.getMessage(), ex);
        return ApiResult.failure(SystemResultCode.MISSING_REQ_DATA).message(ex.getMessage());
    }

    /**
     * 请求方法不支持
     * @param ex
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public ApiResult<String> handleMethodArgumentNotValidException(HttpRequestMethodNotSupportedException ex) {
        log.error(ex.getMessage(), ex);
        return ApiResult.failure(SystemResultCode.METHOD_NOT_ALLOWED);
    }

    /**
     * 请求方法不支持
     * @param ex
     * @return
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ResponseBody
    public ApiResult<String> httpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        log.error(ex.getMessage(), ex);
        return ApiResult.failure(SystemResultCode.UNSUPPORTED_MEDIA_TYPE);
    }

    /**
     * 处理请求参数格式错误 @RequestParam上validate失败后抛出的异常是javax.validation.ConstraintViolationException
     * 处理接口数据验证异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiResult handleMethodArgumentNotValidException(ConstraintViolationException ex) {
        log.error(ex.getMessage(), ex);
        Iterator<ConstraintViolation<?>> iterator = ex.getConstraintViolations().iterator();
        String message = null;
        if (iterator.hasNext()) {
            message = iterator.next().getMessage();
        }
        return ApiResult.failure(SystemResultCode.PARAM_IS_INVALID).message(message);
    }

    /**
     * 处理请求参数格式错误 @RequestParam上validate失败后抛出的异常是javax.validation.ConstraintViolationException
     * 处理接口数据验证异常
     */
    @ExceptionHandler(MissingServletRequestPartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiResult missingServletRequestPartException(MissingServletRequestPartException ex) {
        log.error(ex.getMessage(), ex);
        return ApiResult.failure(SystemResultCode.PARAM_IS_INVALID).message(ex.getMessage());
    }

    /**
     * 处理请求参数格式错误 @RequestBody上validate失败后抛出的异常是MethodArgumentNotValidException异常。
     * @param ex
     * @return
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiResult methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return ApiResult.failure(SystemResultCode.MISSING_REQ_DATA).message("缺少参数 " + ex.getName());
    }

    /**
     *  aop参数校验抛出的异常
     * @param ex
     * @return
     */
    @ExceptionHandler(ValidatorFormException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResult paramException(ValidatorFormException ex){
        return ApiResult.failure(SystemResultCode.PARAM_IS_INVALID).data(ex.getData());
    }

    /**
     * 处理请求参数格式错误 @RequestBody上validate失败后抛出的异常是MethodArgumentNotValidException异常。
     * @param ex
     * @return
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiResult processValidationError(Exception ex) {
        log.error(ex.getMessage());
        BindingResult bindResult = null;
        if (ex instanceof BindException) {
            bindResult = ((BindException) ex).getBindingResult();
        } else if (ex instanceof MethodArgumentNotValidException) {
            bindResult = ((MethodArgumentNotValidException) ex).getBindingResult();
        }
        if(bindResult == null){
            return ApiResult.failure(SystemResultCode.PARAM_IS_INVALID);
        }
        ValidationErrorDTO validationErrorDTO = formValidator.processFieldErrors(bindResult.getFieldErrors());
        return ApiResult.failure(SystemResultCode.PARAM_IS_INVALID).data(validationErrorDTO.getFirstErrorMsg());
    }

    @ExceptionHandler({Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiResult handleAllUnCatchException(Exception ex) {
        log.error(ex.getMessage(), ex);
        showStackTraceInfo(ex);
        return ApiResult.failure(SystemResultCode.SYSTEM_INNER_ERROR);
    }

    @ExceptionHandler({SQLException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiResult sqlException(SQLException ex) {
        log.error(ex.getMessage(), ex);
        showStackTraceInfo(ex);
        return ApiResult.failure(SystemResultCode.SYSTEM_INNER_ERROR).message("数据库异常." + ex.getMessage());
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ApiResult duplicateKeyException(DuplicateKeyException ex){
        log.error(ex.getMessage(), ex);
        return ApiResult.failure(SystemResultCode.EXISTED);
    }

    @ExceptionHandler(BusinessException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ApiResult businessException(BusinessException ex) {
        if(ex.isPrintLog()){
            log.error(ex.getMessage(), ex);
        }else{
            log.error("{}", ex.getMessage());
        }
        showStackTraceInfo(ex);
        return ApiResult.failure().code(ex.getErrorCode()).message(ex.getMessage()).data(ex.getData());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ApiResult illegalArgumentException(IllegalArgumentException ex) {
        showStackTraceInfo(ex);
        return ApiResult.failure(ex.getMessage());
    }

    /**
     * security 异常
     * @param ex
     * @return
     */
    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResult authenticationException(AuthenticationException ex) {
        log.error(ex.getMessage(), ex);
        return ApiResult.failure(ex.getMessage());
    }

    /**
     * securtiy 异常
     * @param ex
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResult accessDeniedException(AccessDeniedException ex) {
        log.error(ex.getMessage(), ex);
        return ApiResult.<String>failure(ex.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResult badCredentialsException(BadCredentialsException ex) {
        log.error(ex.getMessage(), ex);
        return ApiResult.<String>failure("无效账号或密码");
    }

    @ExceptionHandler(LimitException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResult limitException(LimitException ex) {
        return ApiResult.<String>failure("已限流,请慢点.");
    }


    /**
     * 显示异常行号等信息
     */
    private void showStackTraceInfo(Exception ex){
        StackTraceElement currentStackTrace = ex.getStackTrace()[0];
        if(currentStackTrace != null){
            log.error("File: {}", currentStackTrace.getFileName());
            log.error("Class: [{}] {} - <{}>", currentStackTrace.getLineNumber(), currentStackTrace.getClassName(), currentStackTrace.getMethodName());
        }
    }
}
