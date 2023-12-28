package com.wudgaby.platform.core.support;

import com.google.common.collect.Maps;
import com.wudgaby.platform.core.annotation.ValidateForm;
import com.wudgaby.platform.core.model.form.BaseValidateForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Path;
import javax.validation.Validator;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2018/9/25/025 20:55
 * @Desc :
 */
@Slf4j
@Component
public class CustomFormValidator {
    @Resource private Validator globalValidator;

    private <T> Set<ConstraintViolation<T>> validMethodParams(T obj, Method method, Object [] params){
        return globalValidator.forExecutables().validateParameters(obj, method, params);
    }

    private <T> Set<ConstraintViolation<T>> validBeanParams(T bean) {
        return globalValidator.validate(bean);
    }

    public Map<String, Object> validate(Object [] params){
        Map<String, Object> paramErrorMap = Maps.newHashMap();

        for(Object arg : params){
            if(arg == null){
                continue;
            }
            Class<?>[] interfaces = arg.getClass().getInterfaces();
            Annotation validatorAnnotation = arg.getClass().getAnnotation(ValidateForm.class);
            log.info("{}, {}, {}", new Object[]{arg, interfaces, validatorAnnotation});

            //提供接口 和 注解方式
            boolean isValidatorForm = arg instanceof BaseValidateForm || validatorAnnotation != null;

            if(isValidatorForm){
                Set<ConstraintViolation<Object>> constraintViolations = validBeanParams(arg);
                for (ConstraintViolation<Object> constraintViolation : constraintViolations) {
                    Path property = constraintViolation.getPropertyPath();
                    String name = property.iterator().next().getName();
                    String message = constraintViolation.getMessage();
                    log.info(name+" "+ message);
                    paramErrorMap.put(name, message);
                }
            }
        }
        return paramErrorMap;
    }
}
