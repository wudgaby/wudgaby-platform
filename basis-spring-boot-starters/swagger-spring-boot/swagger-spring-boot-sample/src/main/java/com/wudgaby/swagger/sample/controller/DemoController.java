package com.wudgaby.swagger.sample.controller;

import com.wudgaby.platform.core.result.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName : DemoController
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/5/9 15:19
 * @Desc :   TODO
 */
@Api(tags = "测试")
@RestController
public class DemoController {
    @ApiOperation("你好")
    @GetMapping("/hello")
    public ApiResult hello(){
        return ApiResult.success().message("hello");
    }
}
