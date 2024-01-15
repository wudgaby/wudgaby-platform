package com.wudgaby.starter.swagger.configuration;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author :  wudgaby
 * @version :  since 1.0
 * @date :  2021/5/2 11:37
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
