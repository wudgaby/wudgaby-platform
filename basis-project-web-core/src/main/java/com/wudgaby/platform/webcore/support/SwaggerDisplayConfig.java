package com.wudgaby.platform.webcore.support;

import com.fasterxml.classmate.ResolvedType;
import com.google.common.base.Optional;
import com.wudgaby.platform.core.annotation.SwaggerDisplayEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ModelPropertyBuilder;
import springfox.documentation.schema.Annotations;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelPropertyBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelPropertyContext;
import springfox.documentation.swagger.schema.ApiModelProperties;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName : ICodeEnumPropertyPlugin
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/11/4 15:45
 *
 * 对swagger展示进行修改
 * 1. 数字、Boolean属性设置默认的example
 * 2. 支持将枚举变量的描述按照枚举类定义展示
 *
 * @see SwaggerDisplayEnum
 * @see ApiModelProperty
 */
@Component
@Primary
@Slf4j
public class SwaggerDisplayConfig implements ModelPropertyBuilderPlugin {

    /**
     * 是否允许swagger
     */
    @Value("${swagger.enabled:false}")
    private Boolean enableSwagger;

    @Override
    public void apply(ModelPropertyContext context) {
        //如果不支持swagger的话，直接返回
        if (!enableSwagger) {
            return;
        }

        //获取当前字段的类型
        final Class fieldType = context.getBeanPropertyDefinition().get().getField().getRawType();

        //设置对数字的字段设置默认的example
        setDefaultExample(context, fieldType);

        //为枚举字段设置注释
        descForEnumFields(context, fieldType);
    }

    /**
     * 为枚举字段设置注释
     */
    private void descForEnumFields(ModelPropertyContext context, Class fieldType) {
        Optional<ApiModelProperty> annotation = getApiModelPropertyAnnotation(context);

        //没有@ApiModelProperty 或者 notes 属性没有值，直接返回 || StringUtils.isBlank((annotation.get()).notes())
        if (!annotation.isPresent() ) {
            return;
        }

        //@ApiModelProperties中的notes指定的class类型
       /* Class rawPrimaryType;
        try {
            rawPrimaryType = Class.forName((annotation.get()).notes());
        } catch (ClassNotFoundException e) {
            //如果指定的类型无法转化，直接忽略
            return;
        }*/
        Class rawPrimaryType = fieldType;

        //如果对应的class是一个@SwaggerDisplayEnum修饰的枚举类，获取其中的枚举值
        Object[] subItemRecords = null;
        SwaggerDisplayEnum swaggerDisplayEnum = AnnotationUtils.findAnnotation(rawPrimaryType, SwaggerDisplayEnum.class);
        if (null != swaggerDisplayEnum && Enum.class.isAssignableFrom(rawPrimaryType)) {
            subItemRecords = rawPrimaryType.getEnumConstants();
        }
        if (null == subItemRecords) {
            return;
        }

        //从annotation中获取对应的值和描述的变量名
        String valueName = swaggerDisplayEnum.valueName();
        String descName = swaggerDisplayEnum.descName();
        if (StringUtils.isBlank(valueName) || StringUtils.isBlank(descName)) {
            return;
        }

        final List<String> displayValues = Arrays.stream(subItemRecords).filter(Objects::nonNull).map(item -> {
            Class currentClass = item.getClass();

            String value = null;
            String desc = null;
            try {
                Method valMethod = currentClass.getMethod(valueName);
                if(valMethod != null){
                    value = valMethod.invoke(item).toString();
                }
                Method descMethod = currentClass.getMethod(descName);
                if(descMethod != null){
                    desc = descMethod.invoke(item).toString();
                }
                /*Field valueField = currentClass.getField(valueName);
                Field descField = currentClass.getField(descName);
                valueField.setAccessible(true);
                descField.setAccessible(true);
                value = valueField.get(item).toString();
                desc = descField.get(item).toString();*/
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                log.error("获取枚举的属性和值失败, {}", e.getMessage());
                return null;
            }
            return value + ":" + desc;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        String joinText = " (" + String.join("; ", displayValues) + ")";
        try {
            Field mField = ModelPropertyBuilder.class.getDeclaredField("description");
            mField.setAccessible(true);
            joinText = mField.get(context.getBuilder()) + joinText;
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        final ResolvedType resolvedType = context.getResolver().resolve(fieldType);
        context.getBuilder().description(joinText).type(resolvedType);
    }

    /**
     * 设置默认的example
     */
    private void setDefaultExample(ModelPropertyContext context, Class fieldType) {
        Optional<ApiModelProperty> annotation = getApiModelPropertyAnnotation(context);

        //没有@ApiModelProperty 或者 example 属性非空,直接返回.
        if (!annotation.isPresent() || StringUtils.isNotBlank((annotation.get()).example())) {
            return;
        }

        if (Number.class.isAssignableFrom(fieldType)) {
            context.getBuilder().example(1);
        }
        if (Boolean.class.isAssignableFrom(fieldType)) {
            context.getBuilder().example(true);
        }
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }

    private Optional<ApiModelProperty> getApiModelPropertyAnnotation(ModelPropertyContext context){
        Optional<ApiModelProperty> annotation = Optional.absent();

        if (context.getAnnotatedElement().isPresent()) {
            annotation = annotation.or(ApiModelProperties.findApiModePropertyAnnotation(context.getAnnotatedElement().get()));
        }
        if (context.getBeanPropertyDefinition().isPresent()) {
            annotation = annotation.or(Annotations.findPropertyAnnotation(context.getBeanPropertyDefinition().get(), ApiModelProperty.class));
        }
        return annotation;
    }

    public static String toUpperFirstChar(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            ch[0] = (char) (ch[0] - 32);
        }
        return new String(ch);

    }
}