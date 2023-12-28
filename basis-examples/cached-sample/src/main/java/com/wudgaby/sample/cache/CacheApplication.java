package com.wudgaby.sample.cache;

import com.wudgaby.platform.core.result.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

/**
 * @author :  wudgaby
 * @version :  since 1.0
 * @date :  2022/1/20 0020 11:59
 * @Desc :
 */
@SpringBootApplication
@RestController
public class CacheApplication {
    @Autowired private CacheService cacheService;

    public static void main(String[] args) {
        SpringApplication.run(CacheApplication.class, args);
    }

    @GetMapping("/")
    public ApiResult get(@RequestParam String name){
        return ApiResult.success().data(cacheService.getAge(name));
    }

    @GetMapping("/all")
    public ApiResult getAll(){
        return ApiResult.success().data(cacheService.getAll());
    }

    @PutMapping("/")
    public ApiResult update(@RequestParam String name, @RequestParam Integer age){
        cacheService.update(name, age);
        return ApiResult.success();
    }

    @DeleteMapping("/")
    public ApiResult del(@RequestParam String name){
        cacheService.del(name);
        return ApiResult.success();
    }

}
