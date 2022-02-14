package com.wudgaby.platform.webcore.configuration;

import com.wudgaby.platform.webcore.error.SnowyErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/2/14 0014 15:18
 * @desc :
 */
@Configuration
public class ErrorConfig {
    /**
     * 系统错误信息提示重写
     * @return
     */
    @Bean
    public SnowyErrorAttributes snowyErrorAttributes(){
        return new SnowyErrorAttributes();
    }
}
