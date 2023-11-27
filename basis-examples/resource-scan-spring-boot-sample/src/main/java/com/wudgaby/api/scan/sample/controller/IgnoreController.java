package com.wudgaby.api.scan.sample.controller;

import com.wudgaby.platform.core.result.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/11/27 0027 10:23
 * @desc :
 */
@ApiIgnore
@Api(tags = "忽略的模块")
@Controller
@RequestMapping("/ignore")
public class IgnoreController {

    @ApiOperation("忽略测测试")
    @GetMapping("/ignore")
    public ApiResult ignoreTest(){
        return ApiResult.success().message("ignoreTest");
    }

}
