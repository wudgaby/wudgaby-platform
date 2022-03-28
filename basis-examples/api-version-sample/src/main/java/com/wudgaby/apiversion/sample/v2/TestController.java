package com.wudgaby.apiversion.sample.v2;

import com.wudgaby.apiversion.ApiVersion;
import com.wudgaby.apiversion.IgnoreApiVersion;
import com.wudgaby.platform.core.result.ApiResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/3/28 0028 12:02
 * @desc :
 */
@ApiVersion("2.0")
@RestController
public class TestController {
    @GetMapping("/hello")
    public ApiResult hello(){
        return ApiResult.success("hello 2.0");
    }

    @ApiVersion("2.1")
    @GetMapping("/hello")
    public ApiResult hello(@RequestParam String name){
        return ApiResult.success("hello! name:" + name);
    }

}
