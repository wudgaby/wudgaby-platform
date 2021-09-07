package com.wudgaby.swagger.sample.controller3;

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
 * @Desc :
 */
@Api(tags = "组3接口")
@RestController
public class Group3Controller {
    @ApiOperation("问候")
    @GetMapping("/say")
    public ApiResult wold(){
        return ApiResult.success().message("hello");
    }
}
