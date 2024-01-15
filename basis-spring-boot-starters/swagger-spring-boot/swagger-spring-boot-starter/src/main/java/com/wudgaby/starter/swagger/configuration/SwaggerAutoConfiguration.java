package com.wudgaby.starter.swagger.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author 翟永超
 * Create date：2017/8/7.
 * My blog： http://blog.didispace.com
 */
@ConditionalOnProperty(name = "swagger.enabled", havingValue = "true", matchIfMissing = true)
@Configuration
@Import({
    DefaultSwaggerConfiguration.class, SwaggerConfiguration.class
})
public class SwaggerAutoConfiguration {

}