package com.wudgaby.sample;

import com.wudgaby.starter.resubmit.RepeatSubmit;
import com.wudgaby.starter.resubmit.ResubmitException;
import com.wudgaby.starter.resubmit.UnCheckRepeatSubmit;
import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.*;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2021/3/14 0:46
 * @Desc :
 */
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
