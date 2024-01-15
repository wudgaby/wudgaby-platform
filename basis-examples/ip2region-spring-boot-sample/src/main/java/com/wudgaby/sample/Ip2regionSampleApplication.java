package com.wudgaby.sample;

import cn.hutool.extra.spring.SpringUtil;
import com.wudgaby.starter.ip2region.Ip2RegionHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author :  wudgaby
 * @version :  1.0.0
 * @date :  2022/4/6 0006 17:41
 * @desc :
 */
@Slf4j
@SpringBootApplication
public class Ip2regionSampleApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Ip2regionSampleApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Ip2RegionHelper ip2RegionHelper = SpringUtil.getBean(Ip2RegionHelper.class);
        System.out.println(ip2RegionHelper.binarySearch("113.92.131.245"));
        System.out.println(ip2RegionHelper.binarySearch("14.215.212.37").getAddressAndIsp());
        System.out.println(ip2RegionHelper.binarySearch("125.188.7.127").getAddressAndIsp());
    }
}
