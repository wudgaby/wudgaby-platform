package sample;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.wudgaby.sign.api.Signature;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import sample.vo.Order;

/**
 * @ClassName : SignSampleBootstrap
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2020/6/20 1:45
 * @Desc :
 */
@Slf4j
@SpringBootApplication(scanBasePackages = {"com.wudgaby", "sample"})
@RestController
@EnableKnife4j
@MapperScan(value = {"sample.mapper"})
public class SignSampleBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(SignSampleBootstrap.class, args);
    }

    @Signature
    @GetMapping("/orders/{orderId}")
    public String getOrder(@PathVariable String orderId, @RequestParam String name, @RequestParam Integer amount){
        return "success";
    }

    @Signature
    @PostMapping("/orders")
    public String saveOrder(@RequestBody Order order){
        return "success";
    }


}
