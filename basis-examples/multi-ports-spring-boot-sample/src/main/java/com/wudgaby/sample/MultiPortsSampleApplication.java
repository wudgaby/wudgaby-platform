package com.wudgaby.sample;

import com.wudgaby.platform.core.config.ExcludeRegistry;
import com.wudgaby.platform.core.result.ApiResult;
import com.wudgaby.platform.utils.PathHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Predicate;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2023/4/19 0019 15:14
 * @desc :
 */
@RestController
@SpringBootApplication(scanBasePackages = {"com.wudgaby"})
public class MultiPortsSampleApplication {
    public static void main(String[] args) {
        SpringApplication.run(MultiPortsSampleApplication.class, args);
    }

    @GetMapping("/internal/hello")
    public ApiResult internal(){
        return ApiResult.success().message("internal");
    }

    @GetMapping("/external/hello")
    public ApiResult external(){
        return ApiResult.success().message("external");
    }

    @Bean
    public Predicate<String> excludedPathPredicate(){
        ExcludeRegistry excludeRegistry = ExcludeRegistry.ofStaticResource();
        Predicate<String> predicate = s -> PathHelper.excludedPath(excludeRegistry.getAllExcludePatterns(), s);
        return predicate;
    }

}
