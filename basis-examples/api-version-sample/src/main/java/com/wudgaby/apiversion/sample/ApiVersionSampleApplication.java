package com.wudgaby.apiversion.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/3/28 0028 12:00
 * @desc :
 */
@SpringBootApplication
//@ComponentScan(nameGenerator = SpringBeanNameGenerator.class)
//@MapperScan(value = "**.mapper", markerInterface = BaseMapper.class, nameGenerator = SpringBeanNameGenerator.class)
public class ApiVersionSampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiVersionSampleApplication.class, args);
    }
}
