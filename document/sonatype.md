发布参考: https://www.jianshu.com/p/8f0165c887c7

**setting.xml配置**
```
<profiles>
    <profile>
      <id>gpg</id>
      <properties>
        <gpg.executable>gpg</gpg.executable>
        <gpg.passphrase>your password</gpg.passphrase>
      </properties>
    </profile>
</profiles>
<activeProfiles>
<activeProfile>gpg</activeProfile>
</activeProfiles>

<servers>
    <server>
        <id>oss-snapshots</id>
        <username>Sonatype账号</username>
        <password>Sonatype密码</password>
    </server>
    <server>
        <id>oss-releases</id>
        <username>Sonatype账号</username>
        <password>Sonatype密码</password>
    </server>
</servers>
```

**发布命令**
```
mvn clean deploy -P release-sonatype -DskipTests=true"
```


**snapshot版本需引入**
```
<repositories>
    <repository>
      <id>snapshots-repo</id>
      <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    </repository>
  </repositories>
  ```