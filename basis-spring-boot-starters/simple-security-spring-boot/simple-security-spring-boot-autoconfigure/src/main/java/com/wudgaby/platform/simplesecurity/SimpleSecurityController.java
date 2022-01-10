package com.wudgaby.platform.simplesecurity;

import com.wudgaby.platform.core.result.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2022/1/10 0010 15:14
 * @Desc :
 */
@Slf4j
@RestController
@Api(tags = "简单认证")
@AllArgsConstructor
public class SimpleSecurityController {
    private final SimpleSecurityService simpleSecurityService;

    @ApiOperation("登录")
    @PostMapping("/ss/login")
    @AnonymousAccess
    public ApiResult login(@RequestParam String account, @RequestParam String password){
        simpleSecurityService.login(account, password);
        return ApiResult.success("登录成功");
    }
}