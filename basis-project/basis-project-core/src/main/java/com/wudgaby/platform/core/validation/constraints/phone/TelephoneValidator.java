package com.wudgaby.platform.core.validation.constraints.phone;

import cn.hutool.core.util.StrUtil;
import com.wudgaby.platform.utils.RegexUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2018/9/24/024 3:24
 * @Desc :   
 */
public class TelephoneValidator implements ConstraintValidator<Telephone, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        //可空
        if (StrUtil.isBlank(value)) {
            return true;
        }
        return RegexUtil.isTelephone(value);
    }

    @Override
    public void initialize(Telephone telephone) {
    }
}
