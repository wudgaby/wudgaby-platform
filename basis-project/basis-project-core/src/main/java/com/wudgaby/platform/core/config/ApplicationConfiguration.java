package com.wudgaby.platform.core.config;

import com.wudgaby.platform.core.annotation.EnableOwnFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * @author wudgaby
 */
@Slf4j
@Configuration
@ComponentScan(value = EnableOwnFrame.BASE_PACKAGE,
        excludeFilters = { @ComponentScan.Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
                @ComponentScan.Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class) })
public class ApplicationConfiguration implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("ComponentScan : {}", EnableOwnFrame.BASE_PACKAGE);
    }
}
