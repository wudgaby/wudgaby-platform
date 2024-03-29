package com.wudgaby.sample;

import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.security.core.annotations.AnonymousAccess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author :  wudgaby
 * @version :  since 1.0
 * @date :  2022/1/10 0010 17:34
 * @Desc :
 */
@AnonymousAccess
@RestController
@Slf4j
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
