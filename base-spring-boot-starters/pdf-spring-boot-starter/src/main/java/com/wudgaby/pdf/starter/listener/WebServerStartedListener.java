package com.wudgaby.pdf.starter.listener;

import com.wudgaby.pdf.starter.manager.CustomFontManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @ClassName : ContextRefreshedListener
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/8/29 15:30
 * @Desc :   TODO
 */
@Slf4j
@Component(value = "pdfWebServerStartedListener")
public class WebServerStartedListener {
    @Value("${font-dir:NONE}")
    private String fontDir;

    @Order(Ordered.LOWEST_PRECEDENCE - 1)
    @EventListener(WebServerInitializedEvent.class)
    public void afterStart(WebServerInitializedEvent event) {
        log.info("加载字体.");
        CustomFontManager.initFromFileSys(fontDir);
        log.info("加载字体完成.");
    }
}
