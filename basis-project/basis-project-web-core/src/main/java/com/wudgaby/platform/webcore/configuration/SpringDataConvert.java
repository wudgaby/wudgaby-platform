package com.wudgaby.platform.webcore.configuration;

import com.wudgaby.platform.core.support.CustomStringTrimConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.GenericConversionService;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.annotation.PostConstruct;

/**
 * @ClassName : SpringDataConvert
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/4/18 13:25
 * @Desc :
 */
@ConditionalOnProperty(value = "converter.custom.enabled", havingValue = "true", matchIfMissing = true)
@Configuration
public class SpringDataConvert {
    @Autowired
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @PostConstruct
    public void addConversionConfig(){
        ConfigurableWebBindingInitializer initializer = (ConfigurableWebBindingInitializer) requestMappingHandlerAdapter.getWebBindingInitializer();
        if(initializer.getConversionService() != null){
            GenericConversionService genericConversionService =(GenericConversionService) initializer.getConversionService();
            genericConversionService.addConverter(new CustomStringTrimConverter());
        }
    }
}
