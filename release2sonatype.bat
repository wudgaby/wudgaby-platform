@echo off

echo "===================================execute flatten BEGIN==================================================="
call  mvn flatten:clean flatten:flatten
echo "===================================execute flatten END==================================================="

echo "===================================release BEGIN==================================================="
call mvn clean deploy -DskipTests=true -P release-sonatype -N^

&& mvn clean deploy -DskipTests=true -f ./basis-project -P release-sonatype -pl ./basis-project-core -am^

&& mvn clean deploy -DskipTests=true -f ./basis-spring-boot-starters -P release-sonatype -pl ^
./redis-spring-boot/redis-spring-boot-starter,^
./rate-limiter-spring-boot/rate-limiter-spring-boot-starter,^
./swagger-spring-boot/swagger-spring-boot-starter,^
./resubmit-spring-boot/resubmit-spring-boot-starter,^
./mail-spring-boot/mail-spring-boot-starter ^
-am

&& mvn clean deploy -DskipTests=true -f ./basis-project -P release-sonatype -rf ./basis-project-core^

&& mvn clean deploy -DskipTests=true -f ./basis-spring-boot-starters -P release-sonatype -rf ./mail-spring-boot/mail-spring-boot-starter^

&& mvn clean deploy -DskipTests=true -f ./basis-project-dependencies -P release-sonatype
echo "===================================release END==================================================="