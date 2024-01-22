package com.wudgaby.starter.license.verify.config;

import com.wudgaby.starter.license.verify.enums.InstallEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/1/18 0018 11:19
 * @desc :
 */
@Data
@ConfigurationProperties(prefix = "license")
public class LicenseProp {
    /**
     * 证书 subject
     */
    private String subject;

    /**
     * 公钥别称
     */
    private String publicAlias;

    /**
     * 访问公钥库的密码
     */
    private String storePass;

    /**
     * 证书生成路径
     */
    private String licensePath;

    /**
     * 密钥库存储路径
     */
    private String publicKeysStorePath;

    /**
     * 安装证书的url
     */
    private String importLicenseUrl;

    /**
     * 安装证书模式. 本地文件/导入
     */
    private InstallEnum installType = InstallEnum.IMPORT;

}
