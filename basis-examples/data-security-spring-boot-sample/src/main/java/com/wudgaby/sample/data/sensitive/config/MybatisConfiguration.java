package com.wudgaby.sample.data.sensitive.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName : MybatisConfiguration
 * @Author :  WudGaby
 * @Version :  1.0
 * @Date : 2019/7/8 10:40
 * @Desc :
 */
@Configuration
@MapperScan(value = {"com.wudgaby.sample.data.sensitive.mapper"})
public class MybatisConfiguration {

}
