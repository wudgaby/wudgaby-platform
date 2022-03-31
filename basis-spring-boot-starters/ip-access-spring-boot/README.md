引入
```
<dependency>
    <groupId>com.wudgaby.platform</groupId>
    <artifactId>api-version-spring-boot-starter</artifactId>
    <version>1.0.1-SNAPSHOT</version>
</dependency>
```

snapshot版本需引入
```
<repositories>
    <repository>
      <id>snapshots-repo</id>
      <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    </repository>
</repositories>
```

yml文件
```
ip:
  access:
    enabled: true
    black-list:
      - 192.168.50.1-192.168.50.254
      - 192.168.1.1/24
      - 192.168.2.1
    white-list:
      - 192.168.50.66
    strategy: AUTHORITY_MIXTURE
```