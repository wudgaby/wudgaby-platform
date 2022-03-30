package com.wudgaby.ipaccess.config;

import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/3/30 0030 17:56
 * @desc : 参考jetcache
 */
public class IpAccessCondition extends SpringBootCondition {
    private String[] names;

    public IpAccessCondition(String... names) {
        Objects.requireNonNull(names, "names can't be null");
        Assert.isTrue(names.length > 0, "names length is 0");
        this.names = names;
    }

    @Override
    public ConditionOutcome getMatchOutcome(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        String message = "no match for ";
        boolean match = true;
        for(String name : names){
            String result = conditionContext.getEnvironment().getProperty(name);
            if(result == null) {
                match = false;
                message += name + " ";
            }
        }

        if (match) {
            return ConditionOutcome.match();
        }

        return ConditionOutcome.noMatch(message);
    }
}
