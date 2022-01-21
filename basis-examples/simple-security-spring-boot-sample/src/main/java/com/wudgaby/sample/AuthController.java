package com.wudgaby.sample;

import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.simplesecurity.annotations.AnonymousAccess;
import com.wudgaby.platform.simplesecurity.annotations.AuthPermit;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author :  wudgaby
 * @Version :  since 1.0
 * @Date :  2022/1/10 0010 17:34
 * @Desc :
 */
@RestController
public class AuthController {
    @GetMapping("/noauth")
    @AnonymousAccess
    public ApiResult noauth(){
        return ApiResult.success("noauth");
    }

    @GetMapping("/auth1")
    public ApiResult auth1(){
        return ApiResult.success("auth1");
    }

    @GetMapping("/role1")
    @AuthPermit(roles = {"role1"})
    public ApiResult role1(){
        return ApiResult.success("role1");
    }

    @GetMapping("/role2")
    @AuthPermit(roles = {"role2"})
    public ApiResult role2(){
        return ApiResult.success("role2");
    }

    @GetMapping("/add")
    @AuthPermit(perms = "sys:user:add")
    public ApiResult add(){
        return ApiResult.success("add");
    }

    @GetMapping("/edit")
    @AuthPermit(perms = "sys:user:edit")
    public ApiResult edit(){
        return ApiResult.success("edit");
    }

    @GetMapping("/del")
    @AuthPermit(perms = "sys:user:del")
    public ApiResult del(){
        return ApiResult.success("del");
    }


    @GetMapping("/req1")
    @AuthPermit
    public ApiResult req1(){
        return ApiResult.success("req1");
    }

    @PostMapping("/req2")
    @AuthPermit
    public ApiResult req2(){
        return ApiResult.success("req2");
    }

    @DeleteMapping("/req3")
    @AuthPermit
    public ApiResult req3(){
        return ApiResult.success("req3");
    }
}
