package com.wudgaby.platform.core.config;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @ClassName : SecureRegistry
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/6/289:26
 * @Desc :  拦截排除设置
 */
@Data
public class ExcludeRegistry {
    private final Set<String> defaultExcludePatterns = Sets.newHashSet();
    private final Set<String> excludePatterns = Sets.newHashSet();

    public ExcludeRegistry(){
        //this.defaultExcludePatterns.add("/");
        this.defaultExcludePatterns.add("/actuator/**");
        this.defaultExcludePatterns.add("/v2/api-docs/**");
        this.defaultExcludePatterns.add("/v2/api-docs-ext/**");
        this.defaultExcludePatterns.add("/log/**");
        this.defaultExcludePatterns.add("/error/**");
        this.defaultExcludePatterns.add("/assets/**");
        this.defaultExcludePatterns.add("/static/**");
        this.defaultExcludePatterns.add("/webjars/**");
        this.defaultExcludePatterns.add("/swagger*");
        this.defaultExcludePatterns.add("/swagger*/**");
        this.defaultExcludePatterns.add("/doc.html");
        this.defaultExcludePatterns.add("/csrf");
        this.defaultExcludePatterns.add("/druid/**");
        this.defaultExcludePatterns.add("/codeGen/**");
    }

    public static ExcludeRegistry ofStaticResource(){
        ExcludeRegistry excludeRegistry = new ExcludeRegistry();
        excludeRegistry.excludeStaticResource();
        return excludeRegistry;
    }

    public void excludeStaticResource(){
        this.defaultExcludePatterns.add("/**/*.ico");
        this.defaultExcludePatterns.add("/**/*.js");
        this.defaultExcludePatterns.add("/**/*.html");
        this.defaultExcludePatterns.add("/**/*.htm");
        this.defaultExcludePatterns.add("/**/*.css");
        this.defaultExcludePatterns.add("/**/*.txt");
        this.defaultExcludePatterns.add("/**/*.json");
        this.defaultExcludePatterns.add("/**/*.png");
        this.defaultExcludePatterns.add("/**/*.jpg");
        this.defaultExcludePatterns.add("/**/*.jpeg");
        this.defaultExcludePatterns.add("/**/*.gif");
        this.defaultExcludePatterns.add("/**/*.bmp");
        this.defaultExcludePatterns.add("/favicon.ico");
    }

    /**
     * 设置放行api
     */
    public ExcludeRegistry excludePathPatterns(String... patterns) {
        return excludePathPatterns(Arrays.asList(patterns));
    }

    /**
     * 设置放行api
     */
    public ExcludeRegistry excludePathPatterns(List<String> patterns) {
        if(CollectionUtils.isNotEmpty(patterns)){
            this.excludePatterns.addAll(patterns);
        }
        return this;
    }

    public Set<String> getAllExcludePatterns() {
        Set<String> all = Sets.newHashSet();
        all.addAll(defaultExcludePatterns);
        all.addAll(excludePatterns);
        return ImmutableSet.copyOf(all);
    }
}
