# wudgaby-platform

## 主要功能
自用框架，包含基础组件和业务组件.

## 组件
![basis.png](document%2Fres%2Fbasis.png)
![starters.png](document%2Fres%2Fstarters.png)


## 如何构建
```bat
./install_projects.bat
选择3构建整个项目
```

## 如何使用
### 如何引入依赖
如果需要使用已发布的版本，在 `dependencyManagement` 中添加如下配置.
然后在 `dependencies` 中添加自己所需使用的依赖即可使用
```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>com.wudgaby.platform</groupId>
            <artifactId>basis-project-dependencies</artifactId>
            <version>latest</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

### 引入单个包
```xml
<dependency>
    <groupId>com.wudgaby.platform</groupId>
    <artifactId>api-version-spring-boot-starter</artifactId>
    <version>latest</version>
</dependency>
```

### SNAPSHOT 版本需在maven中加入
```xml
<repositories>
    <repository>
      <id>snapshots-repo</id>
      <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    </repository>
</repositories>
```

## 版本管理规范

项目的版本号格式为 x.x.x 的形式，其中 x 的数值类型为数字，从 0 开始取值，且不限于 0~9 这个范围。项目处于孵化器阶段时，第一位版本号固定使用 0，即版本号为 0.x.x 的格式。
版本号与springboot版本号靠近

* 1.x.x 版本适用于 Spring Boot 2.3.x
* 2.x.x 版本适用于 Spring Boot 2.7.x
* 3.x.x 版本适用于 Spring Boot 3.x.x

## 演示 Demo
为了演示如何使用，项目包含了一个子模块 `basis-examples`。此模块中提供了演示用的 example ，您可以阅读对应的 example 工程下的 readme 文档，根据里面的步骤来体验。


## FAQ
```
java: You aren’t using a compiler supported by lombok, so lombok will not work and has been disabled.
Your processor is: com.sun.proxy.$Proxy26
Lombok supports: OpenJDK javac, ECJ

解决办法：
settings中搜索Compiler，在Idea的全局配置Compiler中添加如下配置：
-Djps.track.ap.dependencies=false
```

## 交流群
QQ: 暂无
钉钉: 暂无

