package com.wudgaby.swagger.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author 翟永超
 * Create date：2017/8/7.
 * My blog： http://blog.didispace.com
 */
@Configuration
@Import({
    SwaggerConfiguration.class, DefaultSwaggerConfiguration.class
})
public class SwaggerAutoConfiguration {

}