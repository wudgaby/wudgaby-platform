package com.wudgaby.platform.core.validation.constraints.urlip;

import com.wudgaby.platform.utils.RegexUtil;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2018/9/24/024 3:24
 * @Desc :
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
