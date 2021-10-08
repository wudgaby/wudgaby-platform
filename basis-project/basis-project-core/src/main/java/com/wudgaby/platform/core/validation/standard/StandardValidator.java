package com.wudgaby.platform.core.validation.standard;

import com.wudgaby.platform.core.util.ApplicationContextUtil;
import lombok.SneakyThrows;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

public class StandardValidator implements ConstraintValidator<CustomValid, Object> {

    private CustomValid customValid;

    @Override
    public void initialize(CustomValid customValid) {
        this.customValid = customValid;
    }

    @Override
    @SneakyThrows
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        //获得需要执行的校验器
        IValidHandler validHandler = ApplicationContextUtil.getBean(customValid.handler());
        //执行校验器校验
        boolean result = Optional.ofNullable(validHandler).map(handler -> handler.valid(value, customValid)).orElse(false);
        //返回结果
        return result;
    }
}
