package com.wudgaby.swagger.sample.controller1;

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
@Api(tags = "组1接口")
@RestController
public class Group1Controller {
    @ApiOperation("问题")
    @GetMapping("/ask")
    public ApiResult ask(){
        return ApiResult.success().message("ss");
    }
}
