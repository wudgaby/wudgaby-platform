package com.wudgaby.swagger.configuration;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2021/5/2 11:37
 * @Desc :
 */
public class SwaggerCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        boolean enabled = Boolean.valueOf(context.getEnvironment().getProperty("swagger.enabled"));
        boolean showDefaultGroup = Boolean.valueOf(context.getEnvironment().getProperty("swagger.showDefaultGroup"));
        return enabled && showDefaultGroup;
    }
}