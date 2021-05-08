package com.wudgaby.logger.sample;

import com.wudgaby.logger.api.AccessLoggerEventListener;
import com.wudgaby.logger.api.event.AccessLoggerAfterEvent;
import com.wudgaby.logger.api.event.AccessLoggerBeforeEvent;
import com.wudgaby.starter.logger.aop.EnableAccessLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

import java.net.InetAddress;

/**
 * @ClassName : LoggerSampleBootstrap
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/4 15:40
 * @Desc :   TODO
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
    }
}
