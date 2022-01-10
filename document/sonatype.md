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
</profiles>

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

mvn clean deploy -P release-sonatype -DskipTests=true       