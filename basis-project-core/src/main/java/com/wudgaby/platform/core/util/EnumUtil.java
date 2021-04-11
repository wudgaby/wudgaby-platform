package com.wudgaby.platform.core.util;

import com.google.common.base.Enums;
import com.wudgaby.platform.core.enums.ICode;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName : EnumUtil
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/7/23 18:02
 * @Desc :   枚举工具类
 */
@UtilityClass
public class EnumUtil {

    /**
     * 判断枚举值是否存在于指定枚举数组中
     * @param enums  枚举数组
     * @param value  枚举值
     * @return
     */
    public static <T> boolean isExist(ICode<T>[] enums, T value) {
        if (value == null) {
            return false;
        }
        for (ICode<T> e : enums) {
            if (value.equals(e.getCode())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据枚举值获取其对应的名字
     * @param enums  枚举列表
     * @param code  枚举值
     * @return       枚举名称
     */
    public static <T> String getNameByCode(ICode<T>[] enums, T code) {
        if (code == null) {
            return "";
        }
        for (ICode e : enums) {
            if (code.equals(e.getCode())) {
                return e.getName();
            }
        }
        return "";
    }

    /**
     * 根据枚举名称获取对应的枚举值
     * @param enums  枚举列表
     * @param name   枚举名
     * @return       枚举值
     */
    public static <T> T getCodeByName(ICode<T>[] enums, String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        }
        for (ICode<T> e : enums) {
            if (name.equals(e.getName())) {
                return e.getCode();
            }
        }
        return  null;
    }

    public static <T> boolean isContain(ICode<T>[] enums, ICode<T> codeEnum) {
        if (codeEnum == null) {
            return false;
        }
        for (ICode<T> e : enums) {
            if (e == codeEnum) {
                return true;
            }
        }
        return false;
    }

    public static boolean isContain(Enum[] enums, Enum findEnum) {
        if (findEnum == null) {
            return false;
        }
        for (Enum e : enums) {
            if (e == findEnum) {
                return true;
            }
        }
        return false;
    }
}
