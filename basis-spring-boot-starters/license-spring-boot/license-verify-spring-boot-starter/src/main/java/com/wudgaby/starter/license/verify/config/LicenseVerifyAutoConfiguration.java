package com.wudgaby.starter.license.verify.config;

import com.wudgaby.starter.license.verify.LicenseVerify;
import com.wudgaby.starter.license.verify.listener.LicenseVerifyInstallListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2024/1/18 0018 11:21
 * @desc :
 */
@Configuration
@EnableConfigurationProperties(LicenseProp.class)
public class LicenseVerifyAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public LicenseVerify licenseVerify(LicenseProp licenseProp) {
        return new LicenseVerify(licenseProp.getSubject(), licenseProp.getPublicAlias(), licenseProp.getStorePass(),
                                    licenseProp.getLicensePath(), licenseProp.getPublicKeysStorePath());
    }

    @Bean
    @ConditionalOnMissingBean
    public LicenseVerifyInstallListener licenseVerifyInstallListener(LicenseProp licenseProp) {
        return new LicenseVerifyInstallListener(licenseProp, licenseVerify(licenseProp));
    }
}
