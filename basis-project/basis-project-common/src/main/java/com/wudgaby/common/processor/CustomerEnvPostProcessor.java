package com.wudgaby.common.processor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName : CustomerEnvPostProcessor
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/5/7 16:42
 * @Desc :   TODO
 */
public class CustomerEnvPostProcessor implements EnvironmentPostProcessor {
    private final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();
    private final List<PropertySourceLoader> propertySourceLoaders;
    private final ResourceLoader resourceLoader = new DefaultResourceLoader();
    private String configFilePath = ResourceUtils.CLASSPATH_URL_PREFIX + "config/default";

    public CustomerEnvPostProcessor(){
        super();
        propertySourceLoaders = SpringFactoriesLoader.loadFactories(PropertySourceLoader.class, getClass().getClassLoader());
    }

    public CustomerEnvPostProcessor(String configFilePath){
        this();
        this.configFilePath = configFilePath;
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        //String[] activeProfiles = environment.getActiveProfiles();

        for (PropertySourceLoader propertySourceLoader : propertySourceLoaders){
            for(String fileExtension : propertySourceLoader.getFileExtensions()){
                try {
                    Resource[] resources = this.resourceResolver.getResources(configFilePath + "." + fileExtension);
                    for(Resource res : resources){
                        loadResource(res, propertySourceLoader, environment);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void loadResource(Resource resource, PropertySourceLoader propertySourceLoader, ConfigurableEnvironment environment){
        if(!resource.exists()){
            return;
        }
        try{
            PropertySource<?> propertySource = loadProperties(propertySourceLoader, resource);
            System.out.println("load configs : " + resource.toString());
            environment.getPropertySources().addLast(propertySource);
        }catch (Exception e){
            e.printStackTrace();
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
