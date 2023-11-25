package com.wudgaby.api.scan.sample.controller;

import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.starter.resource.scan.annotation.ApiScan;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName : DemoController
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/5/9 15:19
 * @Desc :
 */
@Api(tags = "测试")
@RestController
public class DemoController {
    @ApiOperation("demo")
    @GetMapping("/demo")
    public ApiResult demo(){
        return ApiResult.success().message("demo");
    }

    @ApiOperation("demoPost")
    @PostMapping("/demoPost")
    @ApiScan(auth = true)
    public ApiResult demoPost(){
        return ApiResult.success().message("demoPost");
    }
}
