package com.wudgaby.apiversion.sample.v1;

import com.wudgaby.apiversion.ApiVersion;
import com.wudgaby.platform.core.result.ApiResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/3/28 0028 12:02
 * @desc :
 */
@ApiVersion("1.0")
@RestController
public class TestController {
    @GetMapping("/hello")
    public ApiResult hello(){
        return ApiResult.success("hello1.0");
    }

    @ApiVersion("1.1")
    @GetMapping("/hello")
    public ApiResult hello11(){
        return ApiResult.success("hello1.1");
    }
}
