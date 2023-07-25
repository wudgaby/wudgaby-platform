@echo off

echo 1.flatten
echo 2.install
echo 3.all

:main
set /p opt=Enter your option:

if %opt% == 1 goto one
if %opt% == 2 goto two
if %opt% == 3 goto all

echo Invalid option
goto main

:one
echo "===================================execute flatten BEGIN==================================================="
call  mvn flatten:clean flatten:flatten
echo "===================================execute flatten END==================================================="
if %opt% == 1 (
    exit /b 0
)


:two
echo "===================================execute install BEGIN==================================================="
call mvn clean install -DskipTests=true -N^
&& mvn clean install -DskipTests=true -f ./basis-project -pl ./basis-project-core -pl ./basis-project-security-core -am^
&& mvn clean install -DskipTests=true -f ./basis-spring-boot-starters -pl ^
./redis-spring-boot/redis-spring-boot-starter,^
./rate-limiter-spring-boot/rate-limiter-spring-boot-starter,^
./swagger-spring-boot/swagger-spring-boot-starter,^
./resubmit-spring-boot/resubmit-spring-boot-starter,^
./mail-spring-boot/mail-spring-boot-starter,^
./mybatis-plus-helper-spring-boot/mybatis-plus-helper-srping-boot-starter ^
-am^
&& mvn clean install -DskipTests=true -f ./basis-project -rf ./basis-project-core^
&& mvn clean install -DskipTests=true -f ./basis-spring-boot-starters -rf ./mail-spring-boot/mail-spring-boot-starter^
&& mvn clean install -DskipTests=true -f ./basis-apps^
&& mvn clean install -DskipTests=true -f ./basis-project-dependencies
echo "===================================execute install END==================================================="
if %opt% == 1 (
    exit /b 0
)

:all
goto one
goto two
exit /b 0