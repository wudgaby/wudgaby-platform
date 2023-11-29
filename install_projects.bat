@echo off

set VAR_ACTION=install
set VAR_PROFILE=

echo 1.flatten
echo 2.only install
echo 3.full install
echo 4.release to sonatype
echo 5.exit

:main
set /p opt=Enter your option:

if %opt% == 1 call:flatten
if %opt% == 2 call:only_install
if %opt% == 3 goto full_install
if %opt% == 4 goto release2sonatype
if %opt% == 5 goto end

echo Invalid Option
goto main

:flatten
echo "===================================execute flatten BEGIN==================================================="
call  mvn flatten:clean flatten:flatten
echo "===================================execute flatten END==================================================="
goto:eof

:only_install
echo "===================================execute install BEGIN==================================================="
echo "ACTION:%VAR_ACTION%"
echo "PROFILE:%VAR_PROFILE%"

call mvn clean %VAR_ACTION% -DskipTests=true %VAR_PROFILE% -N

echo Clear Done && pause
call mvn clean %VAR_ACTION% -DskipTests=true -f ./basis-project %VAR_PROFILE% -pl ./basis-project-core -pl ./basis-project-security-core -am

echo Build Part Core Done && pause
call mvn clean %VAR_ACTION% -DskipTests=true -f ./basis-spring-boot-starters %VAR_PROFILE% -pl ^
./redis-spring-boot/redis-spring-boot-starter,^
./rate-limiter-spring-boot/rate-limiter-spring-boot-starter,^
./swagger-spring-boot/swagger-spring-boot-starter,^
./resubmit-spring-boot/resubmit-spring-boot-starter,^
./mail-spring-boot/mail-spring-boot-starter,^
./mybatis-plus-helper-spring-boot/mybatis-plus-helper-srping-boot-starter ^
-am

echo Build Part Starters Done && pause
call mvn clean %VAR_ACTION% -DskipTests=true -f ./basis-project %VAR_PROFILE% -rf ./basis-project-core

echo Build Core Done && pause
call mvn clean %VAR_ACTION% -DskipTests=true -f ./basis-spring-boot-starters %VAR_PROFILE% -rf ./mail-spring-boot/mail-spring-boot-starter

echo Build Starters Done && pause
call mvn clean %VAR_ACTION% -DskipTests=true -f ./basis-project-dependencies %VAR_PROFILE%

echo Build Dependencies Done && pause
:: mvn clean %VAR_PROFILE% -DskipTests=true -f ./basis-apps
echo "===================================execute install END==================================================="
goto:eof


:full_install
call:flatten
call:only_install
goto end

:release2sonatype
set VAR_ACTION=deploy
set VAR_PROFILE=-P release-sonatype
call:flatten
call:only_install
goto end

:end
echo "DONE"
exit 0
:: mvn deploy -P release-sonatype -DskipTests=true -f ./basis-spring-boot-starters -rf :signature-spring-boot-autoconfigure
