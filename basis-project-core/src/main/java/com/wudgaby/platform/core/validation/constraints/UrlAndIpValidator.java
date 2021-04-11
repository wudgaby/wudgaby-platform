package com.wudgaby.platform.core.validation.constraints;

import com.wudgaby.platform.core.validation.URLAndIP;
import com.wudgaby.platform.utils.RegexUtil;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @ClassName : PhoneValidator
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2018/9/24/024 3:24
 * @Desc :   TODO
 */
@Slf4j
public class UrlAndIpValidator implements ConstraintValidator<URLAndIP, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value == null || "".equals(value)){
            return true;
        }
        return RegexUtil.isURL(value) || RegexUtil.isIp(value);
    }

    @Override
    public void initialize(URLAndIP phone) {
    }
}
