package com.wudgaby.platform.netty.controller;

import org.springframework.stereotype.Controller;

/**
 * @ClassName : IndexController
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/28 9:47
 * @Desc :   TODO
 */
@Controller
public class IndexController {
    public String index(){
        return "index";
    }
}
