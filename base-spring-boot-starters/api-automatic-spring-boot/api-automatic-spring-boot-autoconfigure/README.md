# 使用说明

作者:WudGaby

版本:${revision}



## 描述

该stater是一个在项目启动后把项目中的api资源提交到mq中.辅助完成自动注册资源的工具.



## 使用

**maven引入**

```xml
<dependency>
    <groupId>net.cnns.ly</groupId>
    <artifactId>resource-auto-register-spring-boot-starter</artifactId>
    <version>${revision}</version>
</dependency>
```



##### 开启使用

```java
@EnableResourceRegisterAutoConfiguration
//如需要消费消息,添加如下注解
@EnableBinding({ResourceSink.class})
public class MicroServiceDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(MicroServiceDemoApplication.class, args);
    }
}
```



##### 接口定义参考

```java
@ApiOperation(value = "新增实体", notes = "接口描述")
@GetMapping("/member/{userName}/hello")
```



##### mq消息消费

消费消息时,注意去重.

```java
@StreamListener(ResourceSink.INPUT)
public void receiveResource(@Payload ApiDTO apiDTO) {
    log.info("收到的消息: {}", apiDTO);
}
```

