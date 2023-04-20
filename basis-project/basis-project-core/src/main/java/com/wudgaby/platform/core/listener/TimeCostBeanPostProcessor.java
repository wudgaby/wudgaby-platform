package com.wudgaby.platform.core.listener;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/4/20 0020 17:10
 * @desc :
 */
@Slf4j
@Component
@ConditionalOnProperty(name = "bean.cost.enabled", havingValue = "true")
public class TimeCostBeanPostProcessor implements BeanPostProcessor {
    private Map<String, Long> costMap = Maps.newConcurrentMap();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        costMap.put(beanName, System.currentTimeMillis());
        return bean;
    }
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (costMap.containsKey(beanName)) {
            Long start = costMap.get(beanName);
            long cost  = System.currentTimeMillis() - start;
            if (cost > 0) {
                costMap.put(beanName, cost);
                String optimize = "";
                if(cost >= 1000) {
                    optimize = "[优化] ";
                }
                log.info("{} COST: {} MS. [{}]. ", optimize, cost, beanName);
            }
        }
        return bean;
    }
}