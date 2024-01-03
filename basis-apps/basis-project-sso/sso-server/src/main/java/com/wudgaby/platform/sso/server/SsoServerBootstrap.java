package com.wudgaby.platform.sso.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/6/1 17:44
 * @Desc :  #todo role, resource表未实现.
 */
@SpringBootApplication(scanBasePackages = "com.wudgaby.platform")
@MapperScan("com.wudgaby.platform.sso.server.mapper")
public class SsoServerBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(SsoServerBootstrap.class, args);
    }
}
