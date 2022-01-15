@echo off

echo "===================================execute flatten BEGIN==================================================="
call  mvn flatten:clean flatten:flatten
@REM call  mvn flatten:clean flatten:flatten -f ./basis-project-dependencies
@REM call  mvn flatten:clean flatten:flatten -f ./basis-project -pl ./basis-project-core -am
@REM call  mvn flatten:clean flatten:flatten -f ./basis-spring-boot-starters
@REM call  mvn flatten:clean flatten:flatten -f ./basis-apps
echo "===================================execute flatten END==================================================="

echo "===================================execute install BEGIN==================================================="
call mvn clean install -DskipTests=true -N
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
call mvn clean install -DskipTests=true -f ./basis-project-dependencies
echo "===================================execute install END==================================================="