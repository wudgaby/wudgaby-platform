package com.wudgaby.ratelimiter.sample;

import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.limiter.core.annotation.RateLimiter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * @ClassName : RateLimiterController
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/5/8 16:49
 * @Desc :   TODO
 */
@Slf4j
@Api(tags = "测试")
@RestController
public class RateLimiterController {

    @RateLimiter(permits = 10, timeout = 5, timeUnit = TimeUnit.SECONDS, acquire = 2)
    @GetMapping("limit")
    @ApiOperation("限流")
    public ApiResult limiterDemo(){
        return ApiResult.success();
    }

    @SneakyThrows
    @GetMapping("test")
    public ApiResult test(){
        for(int i=0; i<100; i++){
            log.info("当前: {}", i);
            Thread.sleep(1000);
        }
        return ApiResult.success();
    }
}