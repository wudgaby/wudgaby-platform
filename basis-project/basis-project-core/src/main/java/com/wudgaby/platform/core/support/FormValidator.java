package com.wudgaby.platform.core.support;

import com.google.common.collect.Maps;
import com.wudgaby.platform.core.model.dto.ValidationErrorDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validator;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName : FormValidtor
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2018/9/25/025 20:55
 * @Desc :
 */
@Slf4j
@Component
public class FormValidator {
    @Resource private Validator globalValidator;

    private <T> Set<ConstraintViolation<T>> validMethodParams(T obj, Method method, Object [] params){
        return globalValidator.forExecutables().validateParameters(obj, method, params);
    }

    private <T> Set<ConstraintViolation<T>> validBeanParams(T bean) {
        return globalValidator.validate(bean);
    }

    public Map<String, Object> validate(Object[] params){
        Map<String, Object> paramErrorMap = Maps.newHashMap();

        for(Object arg : params){
            Set<ConstraintViolation<Object>> constraintViolations = validBeanParams(arg);
            for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
                Path property = constraintViolation.getPropertyPath();
                String name = property.iterator().next().getName();
                String message = constraintViolation.getMessage();
                log.info(name+" "+ message);
                paramErrorMap.put(name, message);
            }
        }
        return paramErrorMap;
    }

    public ValidationErrorDTO processFieldErrors(List<FieldError> fieldErrors){
        ValidationErrorDTO dto = new ValidationErrorDTO();
        for (FieldError fieldError: fieldErrors) {
            dto.addFieldError(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return dto;
    }
}
