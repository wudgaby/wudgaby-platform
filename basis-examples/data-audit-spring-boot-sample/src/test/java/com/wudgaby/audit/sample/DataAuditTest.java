package com.wudgaby.audit.sample;

import com.wudgaby.audit.sample.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/8/1 0001 17:15
 * @desc :
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DataAuditTest {
    @Autowired
    private UserService userService;

    @Test
    public void test() {
        userService.dataAudit();
    }
}
