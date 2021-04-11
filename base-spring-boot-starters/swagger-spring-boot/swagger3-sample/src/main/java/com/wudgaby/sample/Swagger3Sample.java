package com.wudgaby.sample;

import com.wudgaby.platform.core.annotation.EnableOwnFrame;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * /swagger-ui/index.html
 */
@SpringBootApplication(scanBasePackages = {"com.wudgaby"})
public class Swagger3Sample {
    public static void main(String[] args) {
        SpringApplication.run(Swagger3Sample.class, args);
    }
}
