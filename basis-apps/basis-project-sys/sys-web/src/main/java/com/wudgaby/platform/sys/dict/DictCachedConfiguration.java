package com.wudgaby.platform.sys.dict;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/4/6 0006 10:36
 * @desc :
 */
@Configuration
public class DictCachedConfiguration {
    @Bean
    public DictHelper dictHelper(DictCachedService dictCachedService){
        DictHelper.init(dictCachedService);
        return new DictHelper();
    }
}
