package com.wudgaby.swagger.aggregation;

import com.wudgaby.platform.core.result.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 
 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/5/9 15:19
 * @Desc :   
 */
@Api(tags = "本地测试")
@RestController
public class DemoController {
    @ApiOperation("本地")
    @GetMapping("/hi")
    public ApiResult hi(){
        return ApiResult.success().message("hi");
    }
}
