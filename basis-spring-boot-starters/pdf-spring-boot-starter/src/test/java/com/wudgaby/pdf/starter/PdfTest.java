package com.wudgaby.pdf.starter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * @ClassName : PdfTest
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/11/6 18:15
 * @Desc :
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PdfTest {
    @Autowired
    private ThymeleafDemo thymeleafDemo;

    @Test
    public void thymeleafDemo() throws IOException {
        thymeleafDemo.demo();
    }
}
