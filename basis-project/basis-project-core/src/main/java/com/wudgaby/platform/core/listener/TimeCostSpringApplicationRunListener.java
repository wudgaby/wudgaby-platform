package com.wudgaby.platform.core.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.time.LocalDateTime;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/4/20 0020 16:59
 * @desc :
 */
@Slf4j
public class TimeCostSpringApplicationRunListener implements SpringApplicationRunListener {
    public TimeCostSpringApplicationRunListener(SpringApplication sa, String[] args) {
    }

    @Override
    public void starting() {
        log.info("starting {}", LocalDateTime.now());
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
        log.info("environmentPrepared {}", LocalDateTime.now());
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {
        log.info("contextPrepared {}", LocalDateTime.now());
    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
        log.info("contextLoaded {}", LocalDateTime.now());
    }

    @Override
    public void started(ConfigurableApplicationContext context) {
        log.info("started {}", LocalDateTime.now());
    }

    @Override
    public void running(ConfigurableApplicationContext context) {
        log.info("running {}", LocalDateTime.now());
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        log.info("failed {}", LocalDateTime.now());
    }
}
