package com.wudgaby.starter.resource.scan.scanner;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.MD5;
import com.google.common.collect.*;
import com.wudgaby.platform.security.core.annotations.AnonymousAccess;
import com.wudgaby.platform.springext.AntPathRequestMatcher;
import com.wudgaby.platform.springext.RequestMatcher;
import com.wudgaby.starter.resource.scan.consts.ApiScanConst;
import com.wudgaby.starter.resource.scan.enums.ApiStatus;
import com.wudgaby.starter.resource.scan.enums.ApiType;
import com.wudgaby.starter.resource.scan.pojo.ResourceDefinition;
import com.wudgaby.starter.resource.scan.service.ApiRegisterService;
import com.wudgaby.starter.resource.scan.service.PermitUrlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import springfox.documentation.annotations.ApiIgnore;

import java.lang.reflect.Method;
import java.util.*;

/**
 * @ClassName : RequestMappingScanListener
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/8/29 15:30
 * @Desc :  启动后扫描所有接口并注册
 * 层级关系: menu->button->api
 */
@Slf4j
@RequiredArgsConstructor
public class RequestMappingScanListener implements ApplicationListener<ApplicationReadyEvent> {
    private final ApiRegisterService apiRegisterService;
    private final PermitUrlService permitUrlService;

    @Value("${spring.application.name:unknown_app}")
    public String serviceName;

    private static final AntPathMatcher PATH_MATCH = new AntPathMatcher();

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("===扫描注册API资源");
        RequestMappingHandlerMapping bean = event.getApplicationContext().getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);

        ListMultimap<ResourceDefinition, ResourceDefinition> listMultimap = doScan(bean.getHandlerMethods());
        listMultimap.keySet().forEach(item -> {
            log.info("======完成初始化【" + item.getName() + "】模块API资源扫描,共:" + listMultimap.get(item).size() + "个接口");
        });

        Set<ResourceDefinition> resourceDefinitionSet = Sets.newHashSet();
        resourceDefinitionSet.addAll(listMultimap.keySet());
        resourceDefinitionSet.addAll(listMultimap.values());

        log.info("===批量注册API, 共:{}个资源(MENU,BUTTON,API)", resourceDefinitionSet.size());
        apiRegisterService.batchRegister(resourceDefinitionSet);
        log.info("===扫描注册API资源完成");
    }


    private ListMultimap<ResourceDefinition, ResourceDefinition> doScan(Map<RequestMappingInfo, HandlerMethod> handlerMethods) {
        ListMultimap<ResourceDefinition, ResourceDefinition> listMultimap = ArrayListMultimap.create();

        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : handlerMethods.entrySet()) {
            HandlerMethod handlerMethod = m.getValue();

            if (handlerMethod.getBeanType() == BasicErrorController.class) {
                continue;
            }

            ApiIgnore apiIgnoreMethod = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), ApiIgnore.class);
            if (apiIgnoreMethod != null) {
                continue;
            }

            ApiIgnore apiIgnoreType = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), ApiIgnore.class);
            if (apiIgnoreType != null) {
                continue;
            }

            Map<ApiType, ResourceDefinition> map = createResourceDefinitions(handlerMethod.getBeanType(), handlerMethod.getMethod(), m.getKey());
            listMultimap.put(map.get(ApiType.MENU), map.get(ApiType.BUTTON));
        }
        return listMultimap;
    }

    private Map<ApiType, ResourceDefinition> createResourceDefinitions(Class<?> clazz, Method method, RequestMappingInfo requestMappingInfo) {
        PatternsRequestCondition patternsCondition = requestMappingInfo.getPatternsCondition();
        //一个方法可以绑定多个url
        RequestMethodsRequestCondition methodsRequestCondition = requestMappingInfo.getMethodsCondition();

        ApiOperation apiOperation = AnnotationUtils.findAnnotation(method, ApiOperation.class);
        String methodName = Optional.ofNullable(apiOperation).map(ApiOperation::value).orElseGet(() -> {
                    String nm = requestMappingInfo.getName();
                    if (StringUtils.isBlank(nm)) {
                        nm = method.getName();
                    }
                    return nm;
                }
        );

        // 是否需要登录认证
        AnonymousAccess anonymousAccessType = AnnotationUtils.findAnnotation(clazz, AnonymousAccess.class);
        AnonymousAccess anonymousAccessMethod = AnnotationUtils.findAnnotation(method, AnonymousAccess.class);
        boolean isAuth = anonymousAccessType == null;
        if (anonymousAccessMethod != null) {
            isAuth = false;
        }

        Collection<RequestMatcher> permitUrls = Lists.newArrayList();
        if (permitUrlService != null) {
            permitUrls.addAll(permitUrlService.getPermitUrls());
        }
        if (CollectionUtils.isNotEmpty(permitUrls)) {
            // 匹配项目中.permitAll()配置
            for (String url : patternsCondition.getPatterns()) {
                for (RequestMatcher requestMatcher : permitUrls) {
                    if (requestMatcher instanceof AntPathRequestMatcher) {
                        AntPathRequestMatcher pathRequestMatcher = (AntPathRequestMatcher) requestMatcher;
                        if (PATH_MATCH.match(pathRequestMatcher.getPattern(), url)) {
                            // 忽略验证
                            isAuth = false;
                        }
                    }
                }
            }
        }

        String menuName = null;
        Api apiTag = AnnotationUtils.findAnnotation(clazz, Api.class);
        if (apiTag != null && ArrayUtil.isNotEmpty(apiTag.tags()) && StringUtils.isNotBlank(apiTag.value())) {
            menuName = Optional.ofNullable(apiTag.tags()).map(Arrays::toString).orElse(apiTag.value());
        }
        if (StringUtils.isBlank(menuName)) {
            menuName = getModularCode(clazz.getSimpleName());
        }
        menuName = StrUtil.toUnderlineCase(menuName);

        RequestMapping requestMapping = AnnotationUtils.findAnnotation(clazz, RequestMapping.class);
        String reqPath = Optional.ofNullable(requestMapping).map(RequestMapping::path).map(path -> path[0]).orElse(StringUtils.EMPTY);

        ResourceDefinition menuResourceDefinition = new ResourceDefinition()
                .setCode(MD5.create().digestHex16(serviceName + menuName + reqPath))
                .setMethod(StringUtils.EMPTY)
                .setUri(reqPath)
                .setType(ApiType.MENU.name())
                .setServiceName(serviceName)
                .setStatus(ApiStatus.ENABLED)
                .setName(menuName)
                .setAuth(isAuth)
                .setOpen(true)
                .setClassName(clazz.getSimpleName())
                .setMethodName(StringUtils.EMPTY)
                .setDesc(Optional.ofNullable(apiTag).map(Api::description).orElse(StringUtils.EMPTY))
                .setPermissionCode(getMenuPermissionCode(clazz.getSimpleName()));

        String urls = getUrls(patternsCondition.getPatterns());
        String methods = getMethods(methodsRequestCondition.getMethods());
        ResourceDefinition resourceDefinition = new ResourceDefinition()
                .setCode(MD5.create().digestHex16(serviceName + methods + urls))
                .setMethod(methods)
                .setUri(urls)
                .setType(ApiType.BUTTON.name())
                .setServiceName(serviceName)
                .setStatus(ApiStatus.ENABLED)
                .setName(methodName)
                .setAuth(isAuth)
                .setOpen(true)
                .setClassName(clazz.getSimpleName())
                .setMethodName(method.getName())
                .setDesc(Optional.ofNullable(apiOperation).map(ApiOperation::notes).orElse(StringUtils.EMPTY))
                .setPermissionCode(getPermissionCode(clazz.getSimpleName(), method.getName()));

        return ImmutableMap.of(ApiType.MENU, menuResourceDefinition, ApiType.BUTTON, resourceDefinition);
    }

    private String getUrls(Set<String> urls) {
        StringBuilder sbf = new StringBuilder();
        for (String url : urls) {
            sbf.append(url).append(",");
        }
        if (!urls.isEmpty()) {
            sbf.deleteCharAt(sbf.length() - 1);
        }
        return sbf.toString();
    }

    private String getMethods(Set<RequestMethod> requestMethods) {
        StringBuilder sbf = new StringBuilder();
        for (RequestMethod requestMethod : requestMethods) {
            sbf.append(requestMethod.toString()).append(",");
        }
        if (!requestMethods.isEmpty()) {
            sbf.deleteCharAt(sbf.length() - 1);
        }
        return sbf.toString();
    }

    private String getPermissionCode(String className, String methodName) {
        return StrUtil.toUnderlineCase(serviceName)
                + ApiScanConst.LINK_SYMBOL + StrUtil.toUnderlineCase(getModularCode(className))
                + ApiScanConst.LINK_SYMBOL + StrUtil.toUnderlineCase(methodName)
                ;
    }

    private String getMenuPermissionCode(String className) {
        return StrUtil.toUnderlineCase(serviceName)
                + ApiScanConst.LINK_SYMBOL + StrUtil.toUnderlineCase(getModularCode(className))
                ;
    }

    private String getModularCode(String className) {
        // 填充模块编码，模块编码就是控制器名称截取Controller关键字前边的字符串
        int controllerIndex = className.indexOf("Controller");
        if (controllerIndex == -1) {
            log.error(StrUtil.format("{} 命令不规范.请以Controller结尾", className));
            return "";
        }
        return className.substring(0, controllerIndex);
    }
}
