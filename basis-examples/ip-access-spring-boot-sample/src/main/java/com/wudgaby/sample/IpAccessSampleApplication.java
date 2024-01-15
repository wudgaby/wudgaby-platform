package com.wudgaby.sample;

import cn.hutool.core.net.Ipv4Util;
import com.wudgaby.starter.ipaccess.IpManage;
import com.wudgaby.starter.ipaccess.enums.BwType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/3/30 0030 15:46
 * @desc :
 */
@SpringBootApplication
@RestController
public class IpAccessSampleApplication {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(IpAccessSampleApplication.class);
        application.run(args);
    }

    @Autowired
    private IpManage ipManage;

    @GetMapping()
    public String hi(){
        return "hi";
    }

    @PostMapping("/addIp")
    public String addIp(@RequestParam String[] ips, @RequestParam BwType bwType){
        for(String ip : ips) {
            ipManage.batchAdd(Ipv4Util.list(ip, false), bwType);
        }
        return "success";
    }

    @DeleteMapping("delIp")
    public String delIp(@RequestParam String[] ips, @RequestParam BwType bwType){
        for(String ip : ips) {
            ipManage.batchDel(Ipv4Util.list(ip, false), bwType);
        }
        return "success";
    }
}
