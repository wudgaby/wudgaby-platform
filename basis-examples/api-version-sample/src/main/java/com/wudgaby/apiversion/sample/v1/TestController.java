package com.wudgaby.apiversion.sample.v1;

import com.wudgaby.platform.core.model.form.DateRangeForm;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.starter.apiversion.ApiVersion;
import com.wudgaby.starter.apiversion.IgnoreApiVersion;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/3/28 0028 12:02
 * @desc :
 */
@ApiVersion("1.0")
@RestController("V1")
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

    @IgnoreApiVersion
    @GetMapping("/hello")
    public ApiResult helloNone(){
        return ApiResult.success("hello 无版本管理");
    }

    @GetMapping("/dateTest")
    public ApiResult dateTest(DateRangeForm dateRangeForm){
        return ApiResult.success(dateRangeForm);
    }
}
