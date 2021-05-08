package com.wudgaby.apiautomatic.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.boot.env.PropertySourceLoader;
import org.springframework.boot.env.YamlPropertySourceLoader;
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
 * @ClassName : MqConfigEnvironmentPostProcessor
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/8/9 11:13
 * @Desc :
 */
@Slf4j
public class MqConfigEnvironmentPostProcessor implements EnvironmentPostProcessor {

    private final ResourcePatternResolver resourceResolver = new PathMatchingResourcePatternResolver();

    private static final String DEFAULT_CONFIG_PATH = "classpath*:default/";

    private final List<PropertySourceLoader> propertySourceLoaders;
    private final YamlPropertySourceLoader yamlPropertySourceLoader = new YamlPropertySourceLoader();

    public MqConfigEnvironmentPostProcessor() {
        super();
        propertySourceLoaders = SpringFactoriesLoader.loadFactories(PropertySourceLoader.class, getClass().getClassLoader());
    }

    /**
     * 加载默认配置
     *
     * @param environment
     */
    public void loadDefaultConfig(ConfigurableEnvironment environment) {
        System.out.println("加载默认配置! 当前环境: " + StringUtils.arrayToCommaDelimitedString(environment.getActiveProfiles()));
        String propertiesPath = DEFAULT_CONFIG_PATH + "*.yml";
        try {
            Resource[] resources = this.resourceResolver.getResources(propertiesPath);
            if (resources == null || resources.length == 0) {
                System.err.println("未发现任何默认配置.");
                return;
            }
            for (Resource resource : resources) {
                PropertySource<?> propertySource = loadProperties(yamlPropertySourceLoader, resource.getFilename(), resource);
                environment.getPropertySources().addLast(propertySource);
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
