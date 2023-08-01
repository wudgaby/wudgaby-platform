@echo off

set VAR_ACTION=install
set VAR_PROFILE=

echo 1.flatten
echo 2.only install
echo 3.full install
echo 4.release 2 sonatype

:main
set /p opt=Enter your option:

if %opt% == 1 goto flatten
if %opt% == 2 goto only_install
if %opt% == 3 goto full_install
if %opt% == 4 goto release2sonatype

echo Invalid option
goto main

:flatten
echo "===================================execute flatten BEGIN==================================================="
call  mvn flatten:clean flatten:flatten
echo "===================================execute flatten END==================================================="
if %opt% == 1 (
    exit /b 0
)


:only_install
echo "===================================execute install BEGIN==================================================="
echo "ACTION:%VAR_ACTION%"
echo "PROFILE:%VAR_PROFILE%"

call mvn clean %VAR_ACTION% -DskipTests=true %VAR_PROFILE% -N^

&& mvn clean %VAR_ACTION% -DskipTests=true -f ./basis-project %VAR_PROFILE% -pl ./basis-project-core -pl ./basis-project-security-core -am^

&& mvn clean %VAR_ACTION% -DskipTests=true -f ./basis-spring-boot-starters %VAR_PROFILE% -pl ^
./redis-spring-boot/redis-spring-boot-starter,^
./rate-limiter-spring-boot/rate-limiter-spring-boot-starter,^
./swagger-spring-boot/swagger-spring-boot-starter,^
./resubmit-spring-boot/resubmit-spring-boot-starter,^
./mail-spring-boot/mail-spring-boot-starter,^
./mybatis-plus-helper-spring-boot/mybatis-plus-helper-srping-boot-starter ^
-am^

&& mvn clean %VAR_ACTION% -DskipTests=true -f ./basis-project %VAR_PROFILE% -rf ./basis-project-core^

&& mvn clean %VAR_ACTION% -DskipTests=true -f ./basis-spring-boot-starters %VAR_PROFILE% -rf ./mail-spring-boot/mail-spring-boot-starter^

&& mvn clean %VAR_ACTION% -DskipTests=true -f ./basis-project-dependencies %VAR_PROFILE%

:: mvn clean %VAR_PROFILE% -DskipTests=true -f ./basis-apps
echo "===================================execute install END==================================================="
if %opt% == 2 (
    exit /b 0
)

:full_install
goto flatten
goto only_install
exit /b 0

:release2sonatype
set VAR_ACTION=deploy
set VAR_PROFILE=-P release-sonatype
goto flatten
goto only_install
exit /b 0