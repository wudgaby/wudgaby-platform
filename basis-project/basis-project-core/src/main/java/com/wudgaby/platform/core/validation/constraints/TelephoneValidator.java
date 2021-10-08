package com.wudgaby.platform.core.validation.constraints;

import com.wudgaby.platform.utils.RegexUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @ClassName : PhoneValidator
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2018/9/24/024 3:24
 * @Desc :   
 */
public class TelephoneValidator implements ConstraintValidator<Telephone, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        //可空
        if(value == null || "".equals(value)){
            return true;
        }
        return RegexUtil.isTelephone(value);
    }

    @Override
    public void initialize(Telephone telephone) {
    }
}
