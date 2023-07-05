package com.wudgaby.sample;

import com.google.common.collect.Maps;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.starter.crypto.annotation.ApiDecryption;
import com.wudgaby.starter.crypto.annotation.ApiEncryption;
import lombok.Data;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
@SpringBootApplication(scanBasePackages = "com.wudgaby")
@Controller
public class ApiCryptoApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiCryptoApplication.class, args);
    }

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
        return ApiResult.success(userMap.values()).message("返回成功");
    }

    @Data
    static class User{
        private String userId;
        private String username;
        private String password;
        private Integer age;
        private String info;
    }
}
