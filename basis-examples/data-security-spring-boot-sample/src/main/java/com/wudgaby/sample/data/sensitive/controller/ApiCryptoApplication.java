package com.wudgaby.sample.data.sensitive.controller;

import com.google.common.collect.Maps;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.starter.data.security.crypt.annotation.ApiDecryption;
import com.wudgaby.starter.data.security.crypt.annotation.ApiEncryption;
import com.wudgaby.starter.data.security.crypt.annotation.CryptoBean;
import com.wudgaby.starter.data.security.crypt.annotation.CryptoField;
import lombok.Data;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/7/5 0005 9:36
 * @desc :
 */
@Controller
public class ApiCryptoApplication {
    private Map<String, User> userMap = Maps.newHashMap();

    @GetMapping("/")
    public ModelAndView index(){
        return new ModelAndView("index");
    }

    @PostMapping("/users")
    @ResponseBody
    @ApiDecryption
    @ApiEncryption
    public ApiResult addUser(@RequestBody User user){
        userMap.put(user.getUserId(), user);
        user.setPassword("password--test");
        return ApiResult.success(userMap.values()).message("返回成功");
    }

    @CryptoBean
    @Data
    static class User{
        private String userId;
        private String username;
        @CryptoField
        private String password;
        private Integer age;
        @CryptoField
        private String info;
    }
}
