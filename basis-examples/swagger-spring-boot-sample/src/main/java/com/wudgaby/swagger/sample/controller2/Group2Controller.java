package com.wudgaby.swagger.sample.controller2;

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
@Api(tags = "组2接口")
@RestController
public class Group2Controller {
    @ApiOperation("世界")
    @GetMapping("/wold")
    public ApiResult wold(){
        return ApiResult.success().message("wold");
    }
}
