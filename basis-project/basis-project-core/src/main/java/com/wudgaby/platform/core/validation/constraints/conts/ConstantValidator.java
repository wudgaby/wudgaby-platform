package com.wudgaby.platform.core.validation.constraints.conts;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/5/29/029 11:17
 * @Desc :
 */
public class ConstantValidator implements ConstraintValidator<Constant, String> {
    private String constantVal;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(constantVal == null || "".equals(constantVal)){
            return false;
        }
        return constantVal.equals(value);
    }

    @Override
    public void initialize(Constant constraintAnnotation) {
        constantVal = constraintAnnotation.value();
    }
}
