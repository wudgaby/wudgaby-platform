package com.wudgaby.api.scan.sample.controller;

import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.security.core.annotations.AnonymousAccess;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @ClassName : DemoController
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/5/9 15:19
 * @Desc :
 */
@RestController
@RequestMapping("/hello")
public class HelloController {
    @ApiOperation("你好")
    @GetMapping("/hello")
    public ApiResult hello(){
        return ApiResult.success().message("hello");
    }

    @ApiOperation("世界")
    @GetMapping("/world")
    @ApiIgnore
    public ApiResult world(){
        return ApiResult.success().message("world");
    }

    @ApiOperation("hi")
    @GetMapping("/hi")
    @AnonymousAccess
    public ApiResult hi(){
        return ApiResult.success().message("hi");
    }

}
