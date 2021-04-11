package com.wudgaby.swagger.configuration;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.google.common.base.Predicates;
import io.swagger.annotations.ApiOperation;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @ClassName : DefaultSwaggerConfiguration
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/3/20 11:18
 * @Desc :   TODO
 */
@Configuration
@EnableSwagger2
@EnableKnife4j
@ConditionalOnProperty(name = "swagger.enabled", havingValue = "true", matchIfMissing = true)
public class DefaultSwaggerConfiguration {

    @ConditionalOnProperty(name = "swagger.showDefaultGroup", havingValue = "true", matchIfMissing = true)
    @Bean(value = "defaultGroup")
    public Docket defaultGroup() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("所有接口")
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any())
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("默认接口文档")
                .description("默认接口文档")
                .termsOfServiceUrl("http://www.wudgaby.com")
                .contact(new Contact("wudgaby", "www.wudgaby.com", "wudgaby@cnns.net"))
                .version("1.0")
                .build();
    }
}
