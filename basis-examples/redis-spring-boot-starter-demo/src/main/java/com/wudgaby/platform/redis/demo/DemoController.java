package com.wudgaby.platform.redis.demo;

import com.wudgaby.starter.redis.support.RedisSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**

 * @author :  WudGaby
 * @version :  1.0
 * @date : 2020/5/7 12:37
 * @Desc :
 */
@RestController
public class DemoController {
    @Autowired
    private RedisSupport redisSupport;

    @GetMapping("/demo")
    public Object demoReSubmit(){
        DemoVo demoVo = new DemoVo();
        demoVo.setName("wudgaby");
        demoVo.setAge(18);
        redisSupport.set("a", demoVo);
        DemoVo result = (DemoVo)redisSupport.get("a");
        return demoVo;
    }
}
