package com.wudgaby.sample;

import com.wudgaby.plugin.resubmit.RepeatSubmit;
import com.wudgaby.plugin.resubmit.ResubmitException;
import com.wudgaby.plugin.resubmit.UnCheckRepeatSubmit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @ClassName : ResubmitSample
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2021/3/14 0:46
 * @Desc :   TODO
 */
@SpringBootApplication(exclude={
        RedisAutoConfiguration.class
})
@RestController
@RestControllerAdvice
public class ResubmitSampleBootStrap {
    public static void main(String[] args) {
        SpringApplication.run(ResubmitSampleBootStrap.class, args);
    }

    @GetMapping("/resubmit")
    @RepeatSubmit("请勿重复提交")
    public String resubmit(){
        return "resubmit";
    }

    @PostMapping("/post")
    @RepeatSubmit("请勿重复提交")
    public String post(){
        return "post";
    }

    @GetMapping("/uncheck")
    @UnCheckRepeatSubmit
    public String uncheck(){
        return "uncheck";
    }

    @ExceptionHandler(ResubmitException.class)
    public String resubmitException(ResubmitException ex){
        return "异常:" + ex.getMessage();
    }
}
