package com.wudgaby.platform.webcore.support;

import cn.hutool.system.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.ClassUtils;

/**
 * @ClassName : StartedEventListener
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/8/9 15:54
 * @Desc :   springboot启动监听
 */
@Configuration
@Slf4j
public class StartedEventListener {

    @Async
    @Order(Ordered.LOWEST_PRECEDENCE -1)
    @EventListener(WebServerInitializedEvent.class)
    public void afterStart(WebServerInitializedEvent event) {
        log.info("\n------------------------------------\n{}------------------------------------", SystemUtil.getOsInfo());
        log.info("\n------------------------------------\n{}------------------------------------", SystemUtil.getUserInfo());
        log.info("\n------------------------------------\n{}------------------------------------", SystemUtil.getHostInfo());
        log.info("\n------------------------------------\n{}------------------------------------", SystemUtil.getJvmSpecInfo());
        log.info("\n------------------------------------\n{}------------------------------------", SystemUtil.getJvmInfo());
        log.info("\n------------------------------------\n{}------------------------------------", SystemUtil.getJavaInfo());

        Environment environment = event.getApplicationContext().getEnvironment();
        String appName = environment.getProperty("spring.application.name", "UNKNOWN_APP_NAME");
        String contextPath = StringUtils.trimToEmpty(environment.getProperty("server.servlet.context-path", ""));
        boolean sslEnabled = Boolean.valueOf(environment.getProperty("server.ssl.enabled"));

        String protocol = sslEnabled ? "https" : "http";
        int localPort = event.getWebServer().getPort();
        String profile = org.springframework.util.StringUtils.arrayToCommaDelimitedString(environment.getActiveProfiles());

        log.info("===================================================================================");
        log.info("[{}] 启动完成, 协议: [{}], 端口: [{}], 路径: [{}] 环境: [{}]", new Object[]{appName, protocol, localPort, contextPath, profile});
        if(ClassUtils.isPresent("com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j", null)){
            log.info("knife4j Swagger地址: {}://localhost:{}{}/doc.html", protocol, localPort, contextPath);
        }
        if (ClassUtils.isPresent("springfox.documentation.swagger2.annotations.EnableSwagger2", null)) {
            log.info("Swagger地址: {}://localhost:{}{}/swagger-ui.html", protocol, localPort, contextPath);
        }
        if (ClassUtils.isPresent("springfox.documentation.oas.annotations.EnableOpenApi", null)) {
            log.info("Swagger3地址: {}://localhost:{}{}/swagger-ui/index.html", protocol, localPort, contextPath);
        }
        if (ClassUtils.isPresent("com.wudgaby.codegen.annotations.EnableCodeGenUI", null)) {
            log.info("代码生成地址: {}://localhost:{}{}/codegen.html", protocol, localPort, contextPath);
        }
        log.info("===================================================================================");
    }
}