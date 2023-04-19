package com.wudgaby.platform.sys.dict;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/4/6 0006 10:36
 * @desc :
 */
@Configuration
@DependsOn({"flyway", "flywayInitializer"})
public class DictCachedConfiguration {
    @Bean
    public DictHelper dictHelper(DictCachedService dictCachedService){
        DictHelper.init(dictCachedService);
        return new DictHelper();
    }
}
