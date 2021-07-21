package com.wudgaby.redis.starter.support;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.*;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName : RedisEnvironmentPostProcessor
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/4/13/013 18:46
 * @Desc :
 */
@Component
public class RedisEnvironmentPostProcessor implements EnvironmentPostProcessor {
    private final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
    private static final String DEFAULT_MAIL_PROPERTIES_PATH = ResourceUtils.CLASSPATH_URL_PREFIX + "default-redis";
    private final List<PropertySourceLoader> propertySourceLoaders;
    private final ResourceLoader resourceLoader = new DefaultResourceLoader();

    public RedisEnvironmentPostProcessor(){
        super();
        propertySourceLoaders = SpringFactoriesLoader.loadFactories(PropertySourceLoader.class, getClass().getClassLoader());
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        for (PropertySourceLoader propertySourceLoader : propertySourceLoaders){
            for(String fileExtension : propertySourceLoader.getFileExtensions()){
                Resource resource = resourceLoader.getResource(DEFAULT_MAIL_PROPERTIES_PATH + "." + fileExtension);
                if(!resource.exists()){
                    continue;
                }
                try{
                    PropertySource<?> propertySource = loadProperties(propertySourceLoader, resource);
                    System.out.println("load configs : " + resource.toString());
                    environment.getPropertySources().addLast(propertySource);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    private PropertySource<?> loadProperties(PropertySourceLoader loader, Resource resource) {
        if (!resource.exists()) {
            throw new IllegalArgumentException("配置文件 " + resource + " 不存在.");
        }
        try {
            return loader.load("applicationConfig: " + resource.toString(), resource).get(0);
        }catch (IOException ex) {
            throw new IllegalStateException("从 " + resource + " 加载配置文件失败.  ", ex);
        }
    }

}
