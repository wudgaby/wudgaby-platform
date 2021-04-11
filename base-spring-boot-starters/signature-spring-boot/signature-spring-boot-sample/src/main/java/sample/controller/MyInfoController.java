package sample.controller;


import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.openapi.annotations.AccessTokenVerifier;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author WudGaby
 * @since 2020-12-01
 */
@Api(tags = "我的信息")
@RestController
@RequestMapping("/my")
@AccessTokenVerifier
public class MyInfoController {

    @GetMapping
    @ApiOperation("我的")
    public ApiResult my(){
        return ApiResult.success();
    }
}
