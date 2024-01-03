package com.wudgaby.oauth2.uaa;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2019/9/23 17:25
 * @Desc :   https://www.jianshu.com/p/24764aba1012?utm_source=oschina-app
 */
@SpringBootApplication
@MapperScan("com.wudgaby.oauth2.uaa.mapper")
public class UaaBootstrap {
    public static void main(String[] args) {
        new SpringApplicationBuilder(UaaBootstrap.class).web(WebApplicationType.SERVLET).run(args);
    }
}
