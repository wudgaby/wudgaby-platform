package com.wudgaby.platform.sample.controller;

import com.wudgaby.starter.access.limit.AccessLimit;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/11/2 0002 14:09
 * @desc :
 */

@RestController
public class AccessLimiterController {

    @AccessLimit
    @GetMapping("/limitTest")
    public String limitTest(){
        return "limit";
    }

    @AccessLimit(value = "别太快", second = 3L, maxTime = 2L, forbiddenTime = 3L)
    @GetMapping("/limitTest2")
    public String limitTest2(){
        return "limit2";
    }
}
