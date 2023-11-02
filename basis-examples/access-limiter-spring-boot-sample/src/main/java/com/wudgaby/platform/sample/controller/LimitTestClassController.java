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

@AccessLimit
@RestController
public class LimitTestClassController {

    @GetMapping("/class/limitTest")
    public String limitTest(){
        return "class limit";
    }

    @AccessLimit(second = 5, maxTime = 1, forbiddenTime = 5)
    @GetMapping("/class/limitTest2")
    public String limitTest2(){
        return "class limit2";
    }
}
