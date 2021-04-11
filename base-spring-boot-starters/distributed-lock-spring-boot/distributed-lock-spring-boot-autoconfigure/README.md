# distributed-lock-spring-boot-starter 使用说明.md

作者:WudGaby

版本:${revision}



## 描述

该stater是一个基于Redisson的封装.

使用@DistributedLock注解来标识需要使用的分布式锁.

[Redisson Wiki](https://github.com/redisson/redisson/wiki/8.-%E5%88%86%E5%B8%83%E5%BC%8F%E9%94%81%E5%92%8C%E5%90%8C%E6%AD%A5%E5%99%A8)



## 使用

**maven引入**

```maven
<dependency>
    <groupId>com.github.wxiaoqi</groupId>
    <artifactId>distributed-lock-spring-boot-starter</artifactId>
    <version>${revision}</version>
</dependency>
```



**在需要的用到分布式锁的方法中使用@DistributedLock**

```java
@Override
@DistributedLock(keys = {"task"}, lockWait = 10)
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public void lockTest(UserBean bean){
    //你的代码
}
```



**支持el表达式的方式**

```java
@Override
@DistributedLock(keys = {"#bean.userId"}, lockWait = 10)
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public void lockTest(UserBean bean){
    //你的代码
}
```

