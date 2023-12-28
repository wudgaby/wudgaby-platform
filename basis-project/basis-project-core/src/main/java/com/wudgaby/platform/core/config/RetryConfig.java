package com.wudgaby.platform.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/16 23:51
 * @Desc :
 */
@Configuration
public class RetryConfig {
    //@Retryable(value = {SocketTimeoutException.class, IOException.class}, maxAttempts = 2, backoff = @Backoff(delay = 2000L, multiplier = 1))
    @Bean
    public RetryTemplate simpleRetryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        retryTemplate.setRetryPolicy(new SimpleRetryPolicy());
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        backOffPolicy.setInitialInterval(3000);
        backOffPolicy.setMultiplier(2);
        backOffPolicy.setMaxInterval(15000);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        return retryTemplate;
    }
}
