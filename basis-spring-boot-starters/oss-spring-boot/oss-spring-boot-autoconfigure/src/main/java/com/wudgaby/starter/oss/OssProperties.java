package com.wudgaby.starter.oss;

import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/4/7 0007 9:12
 * @desc :
 */
@Validated
@Data
@ConfigurationProperties(OssProperties.PREFIX)
public class OssProperties {
    public static final String PREFIX = "oss";

    /**
     * 服务地址
     */
    @NotEmpty(message = "对象存储服务的URL")
    @URL(message = "对象存储服务的URL格式错误")
    private String url;

    /**
     * 认证账户
     */
    @NotEmpty(message = "对象存储服务认证账户不可为空")
    private String accessKey;

    /**
     * 认证密码
     */
    @NotEmpty(message = "对象存储服务认证密码不可为空")
    private String secretKey;

    /**
     * 区域
     */
    private String region;

    /**
     * 桶名称, 优先级最低
     */
    private String bucket;

    /**
     * 桶不在的时候是否新建桶
     */
    private boolean createBucket = true;

    /**
     * 启动的时候检查桶是否存在
     */
    private boolean checkBucket = true;

    /**
     * 设置HTTP连接、写入和读取超时。值为0意味着没有超时
     * HTTP连接超时，以毫秒为单位。
     */
    private long connectTimeout;

    /**
     * 设置HTTP连接、写入和读取超时。值为0意味着没有超时
     * HTTP写超时，以毫秒为单位。
     */
    private long writeTimeout;

    /**
     * 设置HTTP连接、写入和读取超时。值为0意味着没有超时
     * HTTP读取超时，以毫秒为单位。
     */
    private long readTimeout;

    /**
     * true path-style nginx 反向代理和S3默认支持 pathStyle {http://endpoint/bucketname} false
     * supports virtual-hosted-style 阿里云等需要配置为 virtual-hosted-style
     * 模式{http://bucketname.endpoint}
     */
    private Boolean pathStyleAccess = true;

    /**
     * 端点配置
     */
    private OssHttp http;

    @Data
    public static class OssHttp{
        private Boolean enable;
        private String prefix;
    }
}
