package com.wudgaby.apiautomatic.listeners;

import cn.hutool.crypto.digest.MD5;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.wudgaby.apiautomatic.annotation.ApiRegisterIgnore;
import com.wudgaby.apiautomatic.consts.ApiSystemConst;
import com.wudgaby.apiautomatic.dto.ApiDTO;
import com.wudgaby.apiautomatic.enums.ApiStatus;
import com.wudgaby.apiautomatic.ext.AntPathRequestMatcher;
import com.wudgaby.apiautomatic.ext.RequestMatcher;
import com.wudgaby.apiautomatic.service.ApiRegisterService;
import com.wudgaby.apiautomatic.service.PermitUrlService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * @ClassName : RequestMappingScanListener
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/8/29 15:30
 * @Desc :  启动后扫描所有接口并注册
 */
@Slf4j
public class RequestMappingScanListener implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private ApiRegisterService apiRegisterService;

    @Autowired(required = false)
    private PermitUrlService permitUrlService;

    @Value("${spring.application.name:UNKNOWN_APP_NAME}")
    public String serviceName;

    private static final AntPathMatcher pathMatch = new AntPathMatcher();

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("扫描注册API资源");
        RequestMappingHandlerMapping bean = event.getApplicationContext().getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = bean.getHandlerMethods();

        Set<ApiDTO> apiDTOList = Sets.newHashSet();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : handlerMethods.entrySet()) {
            RequestMappingInfo requestMappingInfo = m.getKey();
            HandlerMethod method = m.getValue();
            PatternsRequestCondition p = requestMappingInfo.getPatternsCondition();
            //一个方法可以绑定多个url
            RequestMethodsRequestCondition methodsRequestCondition = requestMappingInfo.getMethodsCondition();

            ApiRegisterIgnore apiRegisterIgnoreClass = AnnotationUtils.findAnnotation(method.getBeanType(), ApiRegisterIgnore.class);
            if(apiRegisterIgnoreClass != null) {
                continue;
            }

            ApiRegisterIgnore apiRegisterIgnore = AnnotationUtils.findAnnotation(method.getMethod(), ApiRegisterIgnore.class);
            if(apiRegisterIgnore != null) {
                continue;
            }
            ApiIgnore apiIgnore = AnnotationUtils.findAnnotation(method.getMethod(), ApiIgnore.class);
            if(apiIgnore != null) {
                continue;
            }

            ApiOperation apiOperation = AnnotationUtils.findAnnotation(method.getMethod(), ApiOperation.class);
            String name = Optional.ofNullable(apiOperation).map(ap -> ap.value()).orElseGet(() -> {
                        String nm = requestMappingInfo.getName();
                        if (StringUtils.isBlank(nm)) {
                            nm = method.getMethod().getName();
                        }
                        return nm;
                    }
            );

            //List<RequestMatcher> permitUrls = getPermitAllUrls(event.getApplicationContext());
            Collection<RequestMatcher> permitUrls = Lists.newArrayList();
            if(permitUrlService != null) {
                permitUrls.addAll(permitUrlService.getPermitUrls());
            }
            // 是否需要安全认证
            boolean isAuth = true;
            if(CollectionUtils.isNotEmpty(permitUrls)) {
                // 匹配项目中.permitAll()配置
                for (String url : p.getPatterns()) {
                    for (RequestMatcher requestMatcher : permitUrls) {
                        if (requestMatcher instanceof AntPathRequestMatcher) {
                            AntPathRequestMatcher pathRequestMatcher = (AntPathRequestMatcher) requestMatcher;
                            if (pathMatch.match(pathRequestMatcher.getPattern(), url)) {
                                // 忽略验证
                                isAuth = false;
                            }
                        }
                    }
                }
            }

            String urls = getUrls(p.getPatterns());
            String methods = getMethods(methodsRequestCondition.getMethods());
            ApiDTO apiDTO = new ApiDTO()
                    .setCode(MD5.create().digestHex16(serviceName + methods + urls))
                    .setMethod(methods)
                    .setUri(urls)
                    .setType(ApiSystemConst.RES_TYPE)
                    .setServiceName(serviceName)
                    .setStatus(ApiStatus.ENABLED)
                    .setName(name)
                    .setAuth(isAuth)
                    .setOpen(true)
                    .setClassName(method.getBeanType().getName())
                    .setMethodName(method.getMethod().getName())
                    .setDesc(Optional.ofNullable(apiOperation).map(ap -> ap.notes()).orElse(StringUtils.EMPTY));
            apiDTOList.add(apiDTO);
        }
        apiRegisterService.batchRegister(apiDTOList);
    }

    private String getUrls(Set<String> urls) {
        StringBuilder sbf = new StringBuilder();
        for (String url : urls) {
            sbf.append(url).append(",");
        }
        if (urls.size() > 0) {
            sbf.deleteCharAt(sbf.length() - 1);
        }
        return sbf.toString();
    }

    private String getMethods(Set<RequestMethod> requestMethods) {
        StringBuilder sbf = new StringBuilder();
        for (RequestMethod requestMethod : requestMethods) {
            sbf.append(requestMethod.toString()).append(",");
        }
        if (requestMethods.size() > 0) {
            sbf.deleteCharAt(sbf.length() - 1);
        }
        return sbf.toString();
    }

    /*private List<RequestMatcher> getPermitUrls(ConfigurableApplicationContext applicationContext){
        List<RequestMatcher> permitUrls = Lists.newArrayList();
        try {
            // 获取所有安全配置适配器
            Map<String, WebSecurityConfigurerAdapter> securityConfigurerAdapterMap = applicationContext.getBeansOfType(WebSecurityConfigurerAdapter.class);
            Iterator<Map.Entry<String, WebSecurityConfigurerAdapter>> iterable = securityConfigurerAdapterMap.entrySet().iterator();
            while (iterable.hasNext()) {
                WebSecurityConfigurerAdapter configurer = iterable.next().getValue();
                HttpSecurity httpSecurity = (HttpSecurity) ReflectUtil.getFieldValue(configurer,"http");
                FilterSecurityInterceptor filterSecurityInterceptor = httpSecurity.getSharedObject(FilterSecurityInterceptor.class);
                FilterInvocationSecurityMetadataSource metadataSource = filterSecurityInterceptor.getSecurityMetadataSource();
                Map<RequestMatcher, Collection<ConfigAttribute>> requestMap = (Map) ReflectUtil.getFieldValue(metadataSource, "requestMap");
                Iterator<Map.Entry<RequestMatcher, Collection<ConfigAttribute>>> requestIterable = requestMap.entrySet().iterator();
                while (requestIterable.hasNext()) {
                    Map.Entry<RequestMatcher, Collection<ConfigAttribute>> match = requestIterable.next();
                    if (match.getValue().toString().contains("permitAll")) {
                        permitUrls.add(match.getKey());
                    }
                }
            }
        } catch (Exception e) {
            log.error("error:{}", e);
        }
        return permitUrls;
    }*/
}
