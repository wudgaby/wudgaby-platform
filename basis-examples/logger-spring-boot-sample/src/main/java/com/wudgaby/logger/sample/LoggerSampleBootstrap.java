package com.wudgaby.logger.sample;

import cn.hutool.core.util.StrUtil;
import com.wudgaby.logger.api.AccessLoggerEventListener;
import com.wudgaby.logger.api.event.AccessLoggerAfterEvent;
import com.wudgaby.logger.api.event.AccessLoggerBeforeEvent;
import com.wudgaby.logger.api.vo.AccessLoggerInfo;
import com.wudgaby.platform.utils.JacksonUtil;
import com.wudgaby.starter.logger.aop.EnableAccessLogger;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/4 15:40
 * @Desc :
 */
@Slf4j
@SpringBootApplication
@EnableAccessLogger
public class LoggerSampleBootstrap implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(LoggerSampleBootstrap.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("{}", InetAddress.getLocalHost());
    }

    @Bean("listener1")
    public AccessLoggerEventListener listener1(){
        return (accessLoggerInfo) -> {
            log.info("做点什么事1. {}", accessLoggerInfo.toString());
        };
    }

    @Bean("listener2")
    public AccessLoggerEventListener listener2(){
        return (accessLoggerInfo) -> {
            log.info("做点什么事2. {}", accessLoggerInfo.toString());
        };
    }

    @EventListener
    public void accessLoggerBeforeEvent(AccessLoggerBeforeEvent event) {
        log.info("before event: {}", event.getAccessLogInfoVo());
    }

    @EventListener
    public void accessLoggerAfterEvent(AccessLoggerAfterEvent event) {
        log.info("after event: {}", event.getAccessLogInfoVo());
        AccessLoggerInfo loggerInfo = event.getAccessLogInfoVo();

        String contentType = loggerInfo.getHttpHeaders().get("content-type");
        if(StringUtils.isNotBlank(contentType) && contentType.contains("multipart/form-data")){

        }else{
            Map<String, Object> filterMap = loggerInfo.getParameters().entrySet().stream()
                    .filter(entry -> entry.getValue() instanceof Serializable)
                    .collect(LinkedHashMap::new, (m, e) -> m.put(e.getKey(), e.getValue()), LinkedHashMap::putAll);
            JacksonUtil.serialize(filterMap);
        }

        Optional.ofNullable(loggerInfo.getResponse()).map(resp -> {
            if(resp instanceof Serializable){
                String result = JacksonUtil.serialize(resp);
                return StrUtil.sub(result, 0, Math.min(20000, result.length()));
            }
            return null;
        }).orElse(null);
    }
}
