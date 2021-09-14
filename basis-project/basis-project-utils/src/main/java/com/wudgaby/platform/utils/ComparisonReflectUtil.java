package com.wudgaby.platform.utils;

import cn.hutool.core.util.ReflectUtil;
import com.google.common.collect.Lists;
import com.wudgaby.platform.utils.vo.ComparisonFiledInclude;
import com.wudgaby.platform.utils.vo.FieldDiffVO;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @ClassName : ComparisonReflectUtil
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/9/27 10:12
 * @Desc :   对象字段内容比较器
 */
@UtilityClass
public class ComparisonReflectUtil {
    private static final List<Class<?>> WRAPPER = Arrays.asList(Byte.class, Short.class,
                                                                    Integer.class, Long.class, Float.class, Double.class, Character.class,
                                                                    Boolean.class, String.class);

    public static List<FieldDiffVO> resolve(Object source, Object target) {
        List<FieldDiffVO> fieldDiffVOs = Lists.newArrayList();
        if(null == source || null == target) {
            return fieldDiffVOs;
        }
        //取出source类
        Class<?> sourceClass = source.getClass();

        Field[] sourceFields = sourceClass.getDeclaredFields();
        for(Field field : sourceFields) {
            ComparisonFiledInclude fieldAnnotation = field.getAnnotation(ComparisonFiledInclude.class);
            if(fieldAnnotation == null){
                continue;
            }
            String fieldName = field.getName();
            String filedDescName = fieldAnnotation.value();
            Class fieldType = field.getType();


            try {
                // 开启访问权限，否则获取私有字段会报错
                field.setAccessible(true);
                Object sourceVal = source == null ? null : field.get(source);
                Object targetVal = target == null ? null : field.get(target);

                //增对"" 和 null 的处理
                if(fieldType == String.class){
                    sourceVal = Optional.ofNullable(sourceVal).orElse("");
                    targetVal = Optional.ofNullable(targetVal).orElse("");
                }

                boolean isNotEquals = !Objects.equals(sourceVal, targetVal);
                //bigdecimal 使用compareTo比较
                if(fieldType == BigDecimal.class){
                    isNotEquals = ((BigDecimal)sourceVal).compareTo((BigDecimal)targetVal) != 0;
                }
                if(isNotEquals){
                    //日期类型转换
                    if(fieldType == Date.class
                            && StringUtils.isNotBlank(fieldAnnotation.format())){
                        String oldDate = Optional.ofNullable(LocalDateTimeUtil.formatTime(LocalDateTimeUtil.convertDateToLDT((Date)sourceVal), fieldAnnotation.format())).orElse(null);
                        String newDate = Optional.ofNullable(LocalDateTimeUtil.formatTime(LocalDateTimeUtil.convertDateToLDT((Date)targetVal), fieldAnnotation.format())).orElse(null);
                        fieldDiffVOs.add(new FieldDiffVO(filedDescName, fieldAnnotation.ignoreContent(), oldDate, newDate));
                        continue;
                    }

                    //日期类型转换
                    if(fieldType == LocalDateTime.class
                            && StringUtils.isNotBlank(fieldAnnotation.format())){
                        String oldDate = Optional.ofNullable(LocalDateTimeUtil.formatTime((LocalDateTime)sourceVal, fieldAnnotation.format())).orElse(null);
                        String newDate = Optional.ofNullable(LocalDateTimeUtil.formatTime((LocalDateTime)targetVal, fieldAnnotation.format())).orElse(null);
                        fieldDiffVOs.add(new FieldDiffVO(filedDescName, fieldAnnotation.ignoreContent(), oldDate, newDate));
                        continue;
                    }

                    //忽略修改的内容
                    if(fieldAnnotation.ignoreContent()){
                        fieldDiffVOs.add(new FieldDiffVO(filedDescName, fieldAnnotation.ignoreContent(), null, null));
                        continue;
                    }

                    //基础数据类型
                    if(isSimpleField(fieldType)){
                        fieldDiffVOs.add(new FieldDiffVO(filedDescName, fieldAnnotation.ignoreContent(), sourceVal, targetVal));
                    }else if(fieldType.isEnum()){
                        //枚举值处理 枚举中需要有有desc属性
                        if(sourceVal != null){
                            Method sourceDescMethod = sourceVal.getClass().getMethod("getDesc");
                            if(sourceDescMethod != null){
                                sourceVal = sourceDescMethod.invoke(sourceVal);
                            }
                        }

                        if(targetVal != null){
                            Method targetDescMethod = targetVal.getClass().getMethod("getDesc");
                            if(targetDescMethod != null){
                                targetVal = targetDescMethod.invoke(targetVal);
                            }
                        }
                        fieldDiffVOs.add(new FieldDiffVO(filedDescName, fieldAnnotation.ignoreContent(), sourceVal, targetVal));
                    }else{
                        fieldDiffVOs.add(new FieldDiffVO(filedDescName, fieldAnnotation.ignoreContent(), sourceVal, targetVal));
                    }
                }
            }catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                // 只要调用了 field.setAccessible(true) 就不会报这个异常
                throw new IllegalStateException("获取属性进行比对发生异常: " + fieldName, e);
            }
        }
        return fieldDiffVOs;
    }

    /**
     * 是否基本类型
     * @return
     */
    private static boolean isSimpleField(Class<?> clazz) {
        return clazz.isPrimitive() || WRAPPER.contains(clazz);
    }
}
