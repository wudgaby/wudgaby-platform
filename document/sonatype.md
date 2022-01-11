``

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
``

`mvn clean deploy -P gpg,release-sonatype -DskipTests=true -Darguments="gpg.passphrase=password"
`

`此时的构件状态为 Open，选中构件，并点击上方的 **Close–>Confirm **在下边的Activity选项卡中查看状态。

当状态变成closed后，执行 Release–>Confirm 并在下边的Activity选项卡中查看状态。
成功后构件自动删除，一小段时间（约1-2个小时）后即可同步到maven的中央仓库。届时会有邮件通知。

至此，发布到Maven中央仓库完成。`
