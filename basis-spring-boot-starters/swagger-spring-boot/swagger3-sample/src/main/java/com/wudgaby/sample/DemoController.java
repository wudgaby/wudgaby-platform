package com.wudgaby.sample;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@Api(tags = "测试接口")
@RestController
@RequestMapping("/demo")
public class DemoController {

    @ApiOperation("问好")
    @GetMapping("/hello")
    public String hello(@RequestParam String name){
        return "你好!" + name;
    }
}
