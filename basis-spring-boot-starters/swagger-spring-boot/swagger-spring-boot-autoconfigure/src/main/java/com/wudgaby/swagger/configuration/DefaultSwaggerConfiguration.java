package com.wudgaby.swagger.configuration;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.google.common.base.Predicates;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Optional;

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
@EnableConfigurationProperties(SwaggerProperties.class)
@ConditionalOnProperty(name = "swagger.enabled", havingValue = "true", matchIfMissing = true)
@AllArgsConstructor
public class DefaultSwaggerConfiguration {
    private final SwaggerProperties swaggerProperties;

    //@Conditional(SwaggerCondition.class)
    @ConditionalOnMissingBean
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
                .title(Optional.ofNullable(swaggerProperties.getTitle()).orElse("默认接口文档"))
                .description(Optional.ofNullable(swaggerProperties.getDescription()).orElse("默认接口文档"))
                .termsOfServiceUrl(Optional.ofNullable(swaggerProperties.getTermsOfServiceUrl()).orElse("http://www.wudgaby.com"))
                .contact(new Contact(Optional.ofNullable(swaggerProperties.getContact()).map(c->c.getEmail()).orElse("wudgaby"),
                        Optional.ofNullable(swaggerProperties.getContact()).map(c->c.getUrl()).orElse("www.wudgaby.com"),
                        Optional.ofNullable(swaggerProperties.getContact()).map(c->c.getEmail()).orElse("wudgaby@sina.com")))
                .version(Optional.ofNullable(swaggerProperties.getVersion()).orElse("1.0"))
                .build();
    }
}