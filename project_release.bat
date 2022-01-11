@echo off

echo "===================================execute install BEGIN==================================================="
cd /d ./basis-project
call mvn release:prepare


call mvn clean install -DskipTests=true -f ./basis-project -pl ./basis-project-core -am
call mvn clean install -DskipTests=true -f ./basis-spring-boot-starters -pl ^
./redis-spring-boot/redis-spring-boot-starter,^
./rate-limiter-spring-boot/rate-limiter-spring-boot-starter,^
./swagger-spring-boot/swagger-spring-boot-starter,^
./resubmit-spring-boot/resubmit-spring-boot-starter,^
./mail-spring-boot/mail-spring-boot-starter ^
-am
call mvn clean install -DskipTests=true -f ./basis-project
call mvn clean install -DskipTests=true -f ./basis-spring-boot-starters
call mvn clean install -DskipTests=true -f ./basis-apps
echo "===================================execute install END==================================================="