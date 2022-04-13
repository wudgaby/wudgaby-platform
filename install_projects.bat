@echo off

echo "===================================execute flatten BEGIN==================================================="
call  mvn flatten:clean flatten:flatten
echo "===================================execute flatten END==================================================="

echo "===================================execute install BEGIN==================================================="
call mvn clean install -DskipTests=true -N^
&& mvn clean install -DskipTests=true -f ./basis-project -pl ./basis-project-core -am^
&& mvn clean install -DskipTests=true -f ./basis-spring-boot-starters -pl ^
./redis-spring-boot/redis-spring-boot-starter,^
./rate-limiter-spring-boot/rate-limiter-spring-boot-starter,^
./swagger-spring-boot/swagger-spring-boot-starter,^
./resubmit-spring-boot/resubmit-spring-boot-starter,^
./mail-spring-boot/mail-spring-boot-starter ^
-am^
&& mvn clean install -DskipTests=true -f ./basis-project -rf ./basis-project-core^
&& mvn clean install -DskipTests=true -f ./basis-spring-boot-starters -rf ./mail-spring-boot/mail-spring-boot-starter^
&& mvn clean install -DskipTests=true -f ./basis-apps^
&& mvn clean install -DskipTests=true -f ./basis-project-dependencies
echo "===================================execute install END==================================================="