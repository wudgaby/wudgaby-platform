package com.wudgaby.swagger.configuration;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.SpringFactoriesLoader;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

/**
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/8/9 11:13
 * @Desc :
 */
@Slf4j
public class DefaultConfigEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    private static final String DEFAULT_CONFIG_PATH = "classpath*:default/";

    private final List<PropertySourceLoader> propertySourceLoaders;

    public DefaultConfigEnvironmentPostProcessor() {
        super();
        propertySourceLoaders = SpringFactoriesLoader.loadFactories(PropertySourceLoader.class, getClass().getClassLoader());
    }

    /**
     * 加载默认配置
     *
     */
    public void loadDefaultConfig(ConfigurableEnvironment environment) {
        String profile = StringUtils.arrayToCommaDelimitedString(environment.getActiveProfiles());
        System.out.println(this.getClass().getName()+ " 加载默认配置! 当前环境: " + (StringUtils.isEmpty(profile) ? "无" : profile));

        try {
            for (PropertySourceLoader propertySourceLoader : propertySourceLoaders){
                for(String fileExtension : propertySourceLoader.getFileExtensions()){
                    String propertiesPath = DEFAULT_CONFIG_PATH + "*" + fileExtension;

                    Resource[] resources = this.resourceResolver.getResources(propertiesPath);
                    if (resources.length == 0) {
                        System.err.println(StrUtil.format("未发现后缀 {} 默认配置.", fileExtension));
                        continue;
                    }

                    for (Resource resource : resources) {
                        String name = resource.getURI().getPath();
                        if(environment.getPropertySources().get(name) != null){
                            //System.out.println("配置文件: " + name + " 已经加载过.");
                            continue;
                        }
                        PropertySource<?> propertySource = loadProperties(propertySourceLoader, name, resource);
                        environment.getPropertySources().addLast(propertySource);
                    }
                }
            }


        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        loadDefaultConfig(environment);
    }

    private PropertySource<?> loadProperties(PropertySourceLoader loader, String name, Resource path) {
        if (!path.exists()) {
            throw new IllegalArgumentException("配置文件 " + path + " 不存在.");
        }
        try {
            System.out.println("加载配置文件: " + path);
            return loader.load(name, path).get(0);

        } catch (IOException ex) {
            throw new IllegalStateException("从 " + path + " 加载配置文件失败.  ", ex);
        }
    }
}
