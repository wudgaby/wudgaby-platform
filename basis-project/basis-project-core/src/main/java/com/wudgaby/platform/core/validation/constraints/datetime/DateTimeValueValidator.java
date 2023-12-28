package com.wudgaby.platform.core.validation.constraints.datetime;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/15 1:38
 * @Desc :   校验日期格式
 */
public class DateTimeValueValidator implements ConstraintValidator<DateTimeValue, String> {
    private String format;

    @Override
    public void initialize(DateTimeValue dateTimeValue) {
        format = dateTimeValue.format();
    }

    @Override
    public boolean isValid(String dateValue, ConstraintValidatorContext context) {
        //为空则放过，因为在此校验之前会加入@NotNull或@NotBlank校验
        if (ObjectUtil.isEmpty(dateValue)) {
            return true;
        }

        //为空失败
        if(StrUtil.isBlank(format)){
            return false;
        }

        //长度不对直接返回
        if (dateValue.length() != format.length()) {
            return false;
        }

        try {
            DateUtil.parse(dateValue, format);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
