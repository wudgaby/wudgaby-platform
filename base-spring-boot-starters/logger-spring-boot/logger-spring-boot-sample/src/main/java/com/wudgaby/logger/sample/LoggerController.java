package com.wudgaby.logger.sample;

import com.wudgaby.logger.api.annotation.AccessLogger;
import com.wudgaby.platform.core.result.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName : LoggerControler
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/4 15:42
 * @Desc :   TODO
 */
@Api(value = "日志演示", tags = "日志")
@RestController
public class LoggerController {

    @AccessLogger(value = "日志记录演示")
    @ApiOperation(value = "日志demo", notes = "试试")
    @GetMapping("/loggerDemo")
    public ApiResult loggerDemo(@RequestParam String name){
        return ApiResult.success().message("你好:" + name);
    }

}
