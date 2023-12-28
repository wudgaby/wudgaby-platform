package com.wudgaby.platform.sso.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/24 15:57
 * @Desc :
 */
@SpringBootApplication(scanBasePackages = "com.wudgaby.platform")
/*@ComponentScan(value = "com.wudgaby.platform",
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {GlobalExceptionAdvice.class}))*/
public class SsoWebSampleBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(SsoWebSampleBootstrap.class, args);
    }
}
