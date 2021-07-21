package com.wudgaby.platform.core.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Data;
import lombok.Getter;

/**
 * @ClassName : GeneralStatusEnum
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/10/9 21:01
 * @Desc :   
 */
@Getter
public enum GeneralStatusEnum {
    /**正常**/NORMAL,
    /**异常**/ABNORMAL,
    ;

    @EnumValue
    int status;
}
