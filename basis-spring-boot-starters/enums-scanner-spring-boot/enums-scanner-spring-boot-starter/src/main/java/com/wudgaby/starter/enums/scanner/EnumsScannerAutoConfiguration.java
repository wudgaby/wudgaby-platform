package com.wudgaby.starter.enums.scanner;

import com.wudgaby.starter.enums.scanner.annotation.EnumScan;
import com.wudgaby.starter.enums.scanner.cached.EnumCache;
import com.wudgaby.starter.enums.scanner.context.EnumScanProperties;
import com.wudgaby.starter.enums.scanner.context.ExtensionClassPathScanningCandidateComponentProvider;
import com.wudgaby.starter.enums.scanner.context.ResourcesScanner;
import com.wudgaby.starter.enums.scanner.context.TypeFilterProvider;
import com.wudgaby.starter.enums.scanner.handler.EnumScanHandler;
import com.wudgaby.starter.enums.scanner.handler.EnumScanHandlerImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.List;

/**
 * @author: zhuCan
 * @date: 2020/7/9 10:34
 * @description: starter 的自动装配类
 */
@EnableConfigurationProperties(EnumScanProperties.class)
@Configuration
public class EnumsScannerAutoConfiguration {

    @Bean
    public EnumScanHandler enumTable(EnumCache cache,
                                     ResourcesScanner<Class<?>> scanner) {
        return new EnumScanHandlerImpl(cache, scanner);
    }

    /**
     * 默认的内存缓存,当用户重写了一个enumCache并注册为容器时,可覆盖本默认缓存
     *
     * @return 枚举缓存
     */
    @Bean
    @ConditionalOnMissingBean(EnumCache.class)
    public EnumCache enumCache() {
        return new EnumCache.DefaultMemoryEnumCache();
    }

    /**
     * 资料扫描器
     *
     * @param properties          配置
     * @param typeFilterProviders 扩展扫描器过滤条件提供者
     * @return scanner
     */
    @Bean
    @ConditionalOnMissingBean(ResourcesScanner.class)
    public ResourcesScanner<Class<?>> resourcesScanner(EnumScanProperties properties,
                                                       List<TypeFilterProvider> typeFilterProviders,
                                                       ApplicationContext context) {


        return new ExtensionClassPathScanningCandidateComponentProvider(false, x -> {

            if (typeFilterProviders != null) {
                // 增加扫描过滤器
                typeFilterProviders.stream()
                        .map(TypeFilterProvider::filter)
                        .forEach(x::addIncludeFilter);
            }

        }, properties, context);
    }

    /**
     * 扫描器过滤条件提供者
     *
     * @return 扫描器过滤条件提供者
     */
    @Bean
    @ConditionalOnMissingBean(TypeFilterProvider.class)
    public TypeFilterProvider typeFilterProvider() {
        // 设置默认的 filter
        return () -> new AnnotationTypeFilter(EnumScan.class);
    }


}
