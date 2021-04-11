package com.wudgaby.apiautomatic.extend;

import cn.hutool.crypto.digest.MD5;
import com.wudgaby.apiautomatic.dto.ApiDTO;
import com.wudgaby.apiautomatic.enums.ApiStatus;
import com.wudgaby.apiautomatic.service.ResourceService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;
import java.util.Optional;

/**
 * @ClassName : ContextRefreshedListener
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/8/29 15:30
 * @Desc :   TODO
 */
@Slf4j
public class WebServerStartedListener {
    @Autowired
    private ResourceService resourceService;
    @Value("${spring.application.name:UNKNOWN_APP_NAME}")
    public String serviceName;

    @Order(Ordered.LOWEST_PRECEDENCE - 1)
    @EventListener(WebServerInitializedEvent.class)
    public void afterStart(WebServerInitializedEvent event) {
        RequestMappingHandlerMapping bean = event.getApplicationContext().getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = bean.getHandlerMethods();

        for (Map.Entry<RequestMappingInfo, HandlerMethod> m : handlerMethods.entrySet()) {
            RequestMappingInfo requestMappingInfo = m.getKey();
            HandlerMethod method = m.getValue();
            PatternsRequestCondition p = requestMappingInfo.getPatternsCondition();
            RequestMethodsRequestCondition methodsRequestCondition = requestMappingInfo.getMethodsCondition();

            ApiOperation apiOperation = AnnotationUtils.findAnnotation(method.getMethod(), ApiOperation.class);
            String name = Optional.ofNullable(apiOperation).map(ap -> ap.value()).orElseGet(() -> {
                        String nm = requestMappingInfo.getName();
                        if (StringUtils.isBlank(nm)) {
                            nm = method.getMethod().getName();
                        }
                        return nm;
                    }
            );

            for (String url : p.getPatterns()) {
                String urlPattern = RegExUtils.replacePattern(url, "\\{\\w+\\}", "{*}");
                for (RequestMethod requestMethod : methodsRequestCondition.getMethods()) {
                    ApiDTO apiDTO = new ApiDTO().setCode(MD5.create().digestHex16(requestMethod.name() + urlPattern))
                            .setMethod(requestMethod.name())
                            .setUri(urlPattern)
                            .setType("url")
                            .setServiceName(serviceName)
                            .setStatus(ApiStatus.DISABLE)
                            .setName(name)
                            .setDesc(Optional.ofNullable(apiOperation).map(ap -> ap.notes()).orElse(StringUtils.EMPTY));
                    resourceService.register(apiDTO);
                }
            }
        }
    }
}
