package com.wudgaby.platform.core.validation.constraints.phone;

import cn.hutool.core.util.StrUtil;
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
public class PhoneValidator implements ConstraintValidator<Phone, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        //可空
        if (StrUtil.isBlank(value)) {
            return true;
        }
        return RegexUtil.isMobile(value);
    }

    @Override
    public void initialize(Phone phone) {
    }
}
