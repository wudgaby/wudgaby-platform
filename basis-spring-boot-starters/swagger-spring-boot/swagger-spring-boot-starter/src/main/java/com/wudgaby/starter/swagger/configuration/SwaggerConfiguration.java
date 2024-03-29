package com.wudgaby.starter.swagger.configuration;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author :  wudgaby
 * @version :  since 1.0
 * @date :  2021/5/2 11:21
 * @Desc : https://github.com/SpringForAll/spring-boot-starter-swagger/blob/1.9.1.RELEASE/README.md
 *
 */
@AllArgsConstructor
public class SwaggerConfiguration implements BeanFactoryAware {
    private final SwaggerProperties swaggerProperties;
    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Bean
    @ConditionalOnMissingBean
    public List<Docket> createRestApi(SwaggerProperties swaggerProperties) {
        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
        List<Docket> docketList = new LinkedList<>();

        // 没有分组
        if (swaggerProperties.getDocket().size() == 0 && !swaggerProperties.isShowDefaultGroup()) {
            ApiInfo apiInfo = new ApiInfoBuilder()
                    .title(StringUtils.trimToEmpty(swaggerProperties.getTitle()))
                    .description(StringUtils.trimToEmpty(swaggerProperties.getDescription()))
                    .version(StringUtils.trimToEmpty(swaggerProperties.getVersion()))
                    .license(StringUtils.trimToEmpty(swaggerProperties.getLicense()))
                    .licenseUrl(StringUtils.trimToEmpty(swaggerProperties.getLicenseUrl()))
                    .contact(new Contact(swaggerProperties.getContact().getName(),
                            swaggerProperties.getContact().getUrl(),
                            swaggerProperties.getContact().getEmail()))
                    .termsOfServiceUrl(StringUtils.trimToEmpty(swaggerProperties.getTermsOfServiceUrl()))
                    .build();

            Docket docketForBuilder = new Docket(DocumentationType.SWAGGER_2)
                    .host(StringUtils.trimToEmpty(swaggerProperties.getHost()))
                    .apiInfo(apiInfo)
                    .securityContexts(Collections.singletonList(securityContext()))
                    .globalOperationParameters(buildGlobalOperationParametersFromSwaggerProperties(
                            swaggerProperties.getGlobalOperationParameters()));

            if ("BasicAuth".equalsIgnoreCase(swaggerProperties.getAuthorization().getType())) {
                docketForBuilder.securitySchemes(Collections.singletonList(basicAuth()));
            } else if (!"None".equalsIgnoreCase(swaggerProperties.getAuthorization().getType())) {
                docketForBuilder.securitySchemes(Collections.singletonList(apiKey()));
            }

            // 全局响应消息
            if (!swaggerProperties.getApplyDefaultResponseMessages()) {
                buildGlobalResponseMessage(swaggerProperties, docketForBuilder);
            }


            ApiSelectorBuilder apiSelectorBuilder = docketForBuilder.select()
                    .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()));

            // base-path处理
            // 当没有配置任何path的时候，解析/**
            if (swaggerProperties.getBasePath().isEmpty()) {
                swaggerProperties.getBasePath().add("/**");
            }
            for (String path : swaggerProperties.getBasePath()) {
                apiSelectorBuilder.paths(PathSelectors.ant(path));
            }

            //exclude-path处理
            for (String path : swaggerProperties.getExcludePath()) {
                apiSelectorBuilder.paths(PathSelectors.regex("^" +path));
            }

            Docket docket = apiSelectorBuilder.build();

            /* ignoredParameterTypes **/
            Class<?>[] array = new Class[swaggerProperties.getIgnoredParameterTypes().size()];
            Class<?>[] ignoredParameterTypes = swaggerProperties.getIgnoredParameterTypes().toArray(array);
            docket.ignoredParameterTypes(ignoredParameterTypes);

            configurableBeanFactory.registerSingleton("defaultDocket", docket);
            docketList.add(docket);
            return docketList;
        }

        // 分组创建
        for (String key : swaggerProperties.getDocket().keySet()) {
            SwaggerProperties.DocketInfo docketInfo = swaggerProperties.getDocket().get(key);
            String groupName = docketInfo.getGroupName();

            ApiInfo apiInfo = new ApiInfoBuilder()
                    .title(StringUtils.isEmpty(docketInfo.getTitle()) ? swaggerProperties.getTitle() : docketInfo.getTitle())
                    .description(StringUtils.isEmpty(docketInfo.getDescription()) ? swaggerProperties.getDescription() : docketInfo.getDescription())
                    .version(StringUtils.isEmpty(docketInfo.getVersion()) ? swaggerProperties.getVersion() : docketInfo.getVersion())
                    .license(StringUtils.isEmpty(docketInfo.getLicense()) ? swaggerProperties.getLicense() : docketInfo.getLicense())
                    .licenseUrl(StringUtils.isEmpty(docketInfo.getLicenseUrl()) ? swaggerProperties.getLicenseUrl() : docketInfo.getLicenseUrl())
                    .contact(
                            new Contact(
                                    StringUtils.isEmpty(docketInfo.getContact().getName()) ? swaggerProperties.getContact().getName() : docketInfo.getContact().getName(),
                                    StringUtils.isEmpty(docketInfo.getContact().getUrl()) ? swaggerProperties.getContact().getUrl() : docketInfo.getContact().getUrl(),
                                    StringUtils.isEmpty(docketInfo.getContact().getEmail()) ? swaggerProperties.getContact().getEmail() : docketInfo.getContact().getEmail()
                            )
                    )
                    .termsOfServiceUrl(StringUtils.isEmpty(docketInfo.getTermsOfServiceUrl()) ? swaggerProperties.getTermsOfServiceUrl() : docketInfo.getTermsOfServiceUrl())
                    .build();


            Docket docketForBuilder = new Docket(DocumentationType.SWAGGER_2)
                    .host(swaggerProperties.getHost())
                    .groupName(groupName)
                    .apiInfo(apiInfo)
                    .securityContexts(Collections.singletonList(securityContext()))
                    .globalOperationParameters(assemblyGlobalOperationParameters(swaggerProperties.getGlobalOperationParameters(),
                            docketInfo.getGlobalOperationParameters()));

            if ("BasicAuth".equalsIgnoreCase(swaggerProperties.getAuthorization().getType())) {
                docketForBuilder.securitySchemes(Collections.singletonList(basicAuth()));
            } else if (!"None".equalsIgnoreCase(swaggerProperties.getAuthorization().getType())) {
                docketForBuilder.securitySchemes(Collections.singletonList(apiKey()));
            }

            // 全局响应消息
            if (!swaggerProperties.getApplyDefaultResponseMessages()) {
                buildGlobalResponseMessage(swaggerProperties, docketForBuilder);
            }

            ApiSelectorBuilder apiSelectorBuilder = docketForBuilder.select()
                    .apis(RequestHandlerSelectors.basePackage(docketInfo.getBasePackage()));

            // base-path处理
            // 当没有配置任何path的时候，解析/**
            /*if (docketInfo.getBasePath().isEmpty()) {
                docketInfo.getBasePath().add("/**");
            }*/
            for (String path : docketInfo.getBasePath()) {
                apiSelectorBuilder.paths(PathSelectors.ant(path));
            }

            //exclude-path处理
            for (String path : docketInfo.getExcludePath()) {
                apiSelectorBuilder.paths(PathSelectors.regex("^" + path));
            }

            Docket docket = apiSelectorBuilder.build();

            /* ignoredParameterTypes **/
            Class<?>[] array = new Class[docketInfo.getIgnoredParameterTypes().size()];
            Class<?>[] ignoredParameterTypes = docketInfo.getIgnoredParameterTypes().toArray(array);
            docket.ignoredParameterTypes(ignoredParameterTypes);

            String beanName = "docketBeanName" + key;
            configurableBeanFactory.registerSingleton(beanName, docket);
            docketList.add(docket);
        }
        return docketList;
    }

    /**
     * 配置基于 ApiKey 的鉴权对象
     *
     * @return
     */
    private ApiKey apiKey() {
        return new ApiKey(swaggerProperties.getAuthorization().getName(),
                swaggerProperties.getAuthorization().getKeyName(),
                ApiKeyVehicle.HEADER.getValue());
    }

    /**
     * 配置基于 BasicAuth 的鉴权对象
     *
     * @return
     */
    private BasicAuth basicAuth() {
        return new BasicAuth(swaggerProperties.getAuthorization().getName());
    }

    /**
     * 配置默认的全局鉴权策略的开关，以及通过正则表达式进行匹配；默认 ^.*$ 匹配所有URL
     * 其中 securityReferences 为配置启用的鉴权策略
     *
     * @return
     */
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex(swaggerProperties.getAuthorization().getAuthRegex()))
                .build();
    }

    /**
     * 配置默认的全局鉴权策略；其中返回的 SecurityReference 中，reference 即为ApiKey对象里面的name，保持一致才能开启全局鉴权
     *
     * @return
     */
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Collections.singletonList(SecurityReference.builder()
                .reference(swaggerProperties.getAuthorization().getName())
                .scopes(authorizationScopes).build());
    }

    private List<Parameter> buildGlobalOperationParametersFromSwaggerProperties(
            List<SwaggerProperties.GlobalOperationParameter> globalOperationParameters) {
        List<Parameter> parameters = Lists.newArrayList();

        if (Objects.isNull(globalOperationParameters)) {
            return parameters;
        }
        for (SwaggerProperties.GlobalOperationParameter globalOperationParameter : globalOperationParameters) {
            parameters.add(new ParameterBuilder()
                    .name(globalOperationParameter.getName())
                    .description(globalOperationParameter.getDescription())
                    .modelRef(new ModelRef(globalOperationParameter.getModelRef()))
                    .parameterType(globalOperationParameter.getParameterType())
                    .required(Boolean.parseBoolean(globalOperationParameter.getRequired()))
                    .build());
        }
        return parameters;
    }

    /**
     * 局部参数按照name覆盖局部参数
     *
     * @param globalOperationParameters
     * @param docketOperationParameters
     * @return
     */
    private List<Parameter> assemblyGlobalOperationParameters(
            List<SwaggerProperties.GlobalOperationParameter> globalOperationParameters,
            List<SwaggerProperties.GlobalOperationParameter> docketOperationParameters) {

        if (Objects.isNull(docketOperationParameters) || docketOperationParameters.isEmpty()) {
            return buildGlobalOperationParametersFromSwaggerProperties(globalOperationParameters);
        }

        Set<String> docketNames = docketOperationParameters.stream()
                .map(SwaggerProperties.GlobalOperationParameter::getName)
                .collect(Collectors.toSet());

        List<SwaggerProperties.GlobalOperationParameter> resultOperationParameters = Lists.newArrayList();

        if (Objects.nonNull(globalOperationParameters)) {
            for (SwaggerProperties.GlobalOperationParameter parameter : globalOperationParameters) {
                if (!docketNames.contains(parameter.getName())) {
                    resultOperationParameters.add(parameter);
                }
            }
        }

        resultOperationParameters.addAll(docketOperationParameters);
        return buildGlobalOperationParametersFromSwaggerProperties(resultOperationParameters);
    }

    /**
     * 设置全局响应消息
     *
     * @param swaggerProperties swaggerProperties 支持 POST,GET,PUT,PATCH,DELETE,HEAD,OPTIONS,TRACE
     * @param docketForBuilder  swagger docket builder
     */
    private void buildGlobalResponseMessage(SwaggerProperties swaggerProperties, Docket docketForBuilder) {

        SwaggerProperties.GlobalResponseMessage globalResponseMessages =
                swaggerProperties.getGlobalResponseMessage();

        /* POST,GET,PUT,PATCH,DELETE,HEAD,OPTIONS,TRACE 响应消息体 **/
        List<ResponseMessage> postResponseMessages = getResponseMessageList(globalResponseMessages.getPost());
        List<ResponseMessage> getResponseMessages = getResponseMessageList(globalResponseMessages.getGet());
        List<ResponseMessage> putResponseMessages = getResponseMessageList(globalResponseMessages.getPut());
        List<ResponseMessage> patchResponseMessages = getResponseMessageList(globalResponseMessages.getPatch());
        List<ResponseMessage> deleteResponseMessages = getResponseMessageList(globalResponseMessages.getDelete());
        List<ResponseMessage> headResponseMessages = getResponseMessageList(globalResponseMessages.getHead());
        List<ResponseMessage> optionsResponseMessages = getResponseMessageList(globalResponseMessages.getOptions());
        List<ResponseMessage> trackResponseMessages = getResponseMessageList(globalResponseMessages.getTrace());

        docketForBuilder.useDefaultResponseMessages(swaggerProperties.getApplyDefaultResponseMessages())
                .globalResponseMessage(RequestMethod.POST, postResponseMessages)
                .globalResponseMessage(RequestMethod.GET, getResponseMessages)
                .globalResponseMessage(RequestMethod.PUT, putResponseMessages)
                .globalResponseMessage(RequestMethod.PATCH, patchResponseMessages)
                .globalResponseMessage(RequestMethod.DELETE, deleteResponseMessages)
                .globalResponseMessage(RequestMethod.HEAD, headResponseMessages)
                .globalResponseMessage(RequestMethod.OPTIONS, optionsResponseMessages)
                .globalResponseMessage(RequestMethod.TRACE, trackResponseMessages);
    }

    /**
     * 获取返回消息体列表
     *
     * @param globalResponseMessageBodyList 全局Code消息返回集合
     * @return
     */
    private List<ResponseMessage> getResponseMessageList
    (List<SwaggerProperties.GlobalResponseMessageBody> globalResponseMessageBodyList) {
        List<ResponseMessage> responseMessages = new ArrayList<>();
        for (SwaggerProperties.GlobalResponseMessageBody globalResponseMessageBody : globalResponseMessageBodyList) {
            ResponseMessageBuilder responseMessageBuilder = new ResponseMessageBuilder();
            responseMessageBuilder.code(globalResponseMessageBody.getCode()).message(globalResponseMessageBody.getMessage());

            if (StringUtils.isNotEmpty(globalResponseMessageBody.getModelRef())) {
                responseMessageBuilder.responseModel(new ModelRef(globalResponseMessageBody.getModelRef()));
            }
            responseMessages.add(responseMessageBuilder.build());
        }

        return responseMessages;
    }
}