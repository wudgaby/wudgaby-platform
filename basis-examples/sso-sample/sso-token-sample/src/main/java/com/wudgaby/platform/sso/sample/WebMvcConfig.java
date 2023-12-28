package com.wudgaby.platform.sso.sample;

import com.google.common.collect.Lists;
import com.wudgaby.platform.core.config.ExcludeRegistry;
import com.wudgaby.platform.sso.core.config.SsoProperties;
import com.wudgaby.platform.sso.core.constant.SsoConst;
import com.wudgaby.platform.sso.core.helper.SsoRemoteHelper;
import com.wudgaby.platform.sso.core.interceptor.SsoTokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/25 0:08
 * @Desc :
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired private SsoProperties ssoProperties;
    @Autowired private SsoRemoteHelper ssoRemoteHelper;

    @Bean
    public ExcludeRegistry secureRegistry() {
        ExcludeRegistry excludeRegistry = new ExcludeRegistry();
        excludeRegistry.excludePathPatterns(ssoProperties.getExcludedPaths());
        return excludeRegistry;
    }

    /**
     * 认证拦截
     * @return
     */
    @Bean
    public SsoTokenInterceptor ssoTokenInterceptor(){
        return new SsoTokenInterceptor(ssoProperties, ssoRemoteHelper);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(ssoTokenInterceptor())
                    .addPathPatterns("/**")
                    .excludePathPatterns(Lists.newArrayList(secureRegistry().getDefaultExcludePatterns()))
                    .excludePathPatterns(Lists.newArrayList(secureRegistry().getExcludePatterns()))
            ;
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        //解决@PathVariable上的特殊字符
        UrlPathHelper urlPathHelper = new UrlPathHelper();
        urlPathHelper.setUrlDecode(false);
        configurer.setUrlPathHelper(urlPathHelper);
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController(ssoProperties.getLogoutPath());
        registry.addViewController(SsoConst.CODE_EX_TOKEN_URL);
    }
}
