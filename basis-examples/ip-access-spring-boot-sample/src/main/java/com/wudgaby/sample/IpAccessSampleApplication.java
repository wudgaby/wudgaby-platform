package com.wudgaby.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/3/30 0030 15:46
 * @desc :
 */
@SpringBootApplication
@RestController
public class IpAccessSampleApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(IpAccessSampleApplication.class);
        application.run(args);
    }

    @GetMapping()
    public String hi(){
        return "hi";
    }
}
