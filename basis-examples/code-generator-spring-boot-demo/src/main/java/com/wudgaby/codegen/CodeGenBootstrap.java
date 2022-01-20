package com.wudgaby.codegen;

import com.wudgaby.codegen.annotations.EnableCodeGenUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName : CodeGenBootstrap
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/4/3 18:36
 * @Desc :
 */
@EnableCodeGenUI
@SpringBootApplication
public class CodeGenBootstrap implements CommandLineRunner {
    @Autowired
    private TestService testService;

    public static void main(String[] args) {
        SpringApplication.run(CodeGenBootstrap.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println(testService);
    }
}
