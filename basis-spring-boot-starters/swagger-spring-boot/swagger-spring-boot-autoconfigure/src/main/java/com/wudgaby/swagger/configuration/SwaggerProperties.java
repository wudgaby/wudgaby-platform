package com.wudgaby.swagger.configuration;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName : SwaggerProperties
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/5/9 15:02
 * @Desc :   https://github.com/SpringForAll/spring-boot-starter-swagger/blob/1.9.1.RELEASE/README.md
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "swagger")
public class SwaggerProperties {
    private boolean enabled = true;
    private boolean showDefaultGroup = true;

    /**
     * 标题
     **/
    private String title;
    /**
     * 描述
     **/
    private String description;
    /**
     * 版本
     **/
    private String version;
    /**
     * 许可证
     **/
    private String license;
    /**
     * 许可证URL
     **/
    private String licenseUrl;
    /**
     * 服务条款URL
     **/
    private String termsOfServiceUrl;

    /**
     * 忽略的参数类型
     **/
    private List<Class<?>> ignoredParameterTypes = new ArrayList<>();

    private Contact contact = new Contact();

    /**
     * swagger会解析的包路径
     **/
    private String basePackage;

    /**
     * swagger会解析的url规则
     **/
    private List<String> basePath = new ArrayList<>();
    /**
     * 在basePath基础上需要排除的url规则
     **/
    private List<String> excludePath = new ArrayList<>();

    /**
     * 分组文档
     **/
    private Map<String, DocketInfo> docket = new LinkedHashMap<>();

    /**
     * host信息
     **/
    private String host;

    /**
     * 全局参数配置
     **/
    private List<GlobalOperationParameter> globalOperationParameters;

    /**
     * 是否使用默认预定义的响应消息 ，默认 true
     **/
    private Boolean applyDefaultResponseMessages = true;

    /**
     * 全局响应消息
     **/
    private GlobalResponseMessage globalResponseMessage;

    /**
     * 全局统一鉴权配置
     **/
    private Authorization authorization = new Authorization();

    @Data
    @NoArgsConstructor
    public static class GlobalOperationParameter {
        /**
         * 参数名
         **/
        private String name;

        /**
         * 描述信息
         **/
        private String description;

        /**
         * 指定参数类型
         **/
        private String modelRef;

        /**
         * 参数放在哪个地方:header,query,path,body.form
         **/
        private String parameterType;

        /**
         * 参数是否必须传
         **/
        private String required;

    }

    @Data
    @NoArgsConstructor
    public static class DocketInfo {

        /**
         * 分组名称
         */
        private String groupName;
        /**
         * 标题
         **/
        private String title;
        /**
         * 描述
         **/
        private String description;
        /**
         * 版本
         **/
        private String version;
        /**
         * 许可证
         **/
        private String license;
        /**
         * 许可证URL
         **/
        private String licenseUrl;
        /**
         * 服务条款URL
         **/
        private String termsOfServiceUrl;

        private Contact contact = new Contact();

        /**
         * swagger会解析的包路径
         **/
        private String basePackage;

        /**
         * swagger会解析的url规则
         **/
        private List<String> basePath = new ArrayList<>();
        /**
         * 在basePath基础上需要排除的url规则
         **/
        private List<String> excludePath = new ArrayList<>();

        private List<GlobalOperationParameter> globalOperationParameters;

        /**
         * 忽略的参数类型
         **/
        private List<Class<?>> ignoredParameterTypes = new ArrayList<>();

    }

    @Data
    @NoArgsConstructor
    public static class Contact {

        /**
         * 联系人
         **/
        private String name;
        /**
         * 联系人url
         **/
        private String url;
        /**
         * 联系人email
         **/
        private String email;

    }

    @Data
    @NoArgsConstructor
    public static class GlobalResponseMessage {

        /**
         * POST 响应消息体
         **/
        List<GlobalResponseMessageBody> post = new ArrayList<>();

        /**
         * GET 响应消息体
         **/
        List<GlobalResponseMessageBody> get = new ArrayList<>();

        /**
         * PUT 响应消息体
         **/
        List<GlobalResponseMessageBody> put = new ArrayList<>();

        /**
         * PATCH 响应消息体
         **/
        List<GlobalResponseMessageBody> patch = new ArrayList<>();

        /**
         * DELETE 响应消息体
         **/
        List<GlobalResponseMessageBody> delete = new ArrayList<>();

        /**
         * HEAD 响应消息体
         **/
        List<GlobalResponseMessageBody> head = new ArrayList<>();

        /**
         * OPTIONS 响应消息体
         **/
        List<GlobalResponseMessageBody> options = new ArrayList<>();

        /**
         * TRACE 响应消息体
         **/
        List<GlobalResponseMessageBody> trace = new ArrayList<>();

    }

    @Data
    @NoArgsConstructor
    public static class GlobalResponseMessageBody {

        /**
         * 响应码
         **/
        private int code;

        /**
         * 响应消息
         **/
        private String message;

        /**
         * 响应体
         **/
        private String modelRef;

    }

    /**
     * securitySchemes 支持方式之一 ApiKey
     */
    @Data
    @NoArgsConstructor
    static class Authorization {

        /**
         * 鉴权策略ID，对应 SecurityReferences ID
         */
        private String name = "Authorization";

        /**
         * 鉴权策略，可选 ApiKey | BasicAuth | None，默认ApiKey
         */
        private String type = "ApiKey";

        /**
         * 鉴权传递的Header参数
         */
        private String keyName = "TOKEN";

        /**
         * 需要开启鉴权URL的正则
         */
        private String authRegex = "^.*$";
    }

}