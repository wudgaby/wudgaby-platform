package com.wudgaby.sample;

import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.simplesecurity.AnonymousAccess;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2022/1/10 0010 17:34
 * @Desc :
 */
@AnonymousAccess
@RestController
public class SimpleController {
    @GetMapping("/test1")
    public ApiResult test1(){
        return ApiResult.success("test1");
    }

    @GetMapping("/test2")
    public ApiResult test2(){
        return ApiResult.success("test2");
    }
}
