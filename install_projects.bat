@echo off
rem 解决中文乱码
chcp 65001

set VAR_ACTION=install
set VAR_PROFILE=


:main
echo 1.flatten
echo 2.only install
echo 3.full install
echo 4.release to sonatype
echo 5.exit

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

call mvn clean
call mvn %VAR_ACTION% -DskipTests=true %VAR_PROFILE% -pl basis-project/basis-project-web-core -am
call mvn %VAR_ACTION% -DskipTests=true %VAR_PROFILE% -f ./basis-spring-boot-starters
call mvn %VAR_ACTION% -DskipTests=true %VAR_PROFILE% -f ./basis-project-dependencies
call mvn %VAR_ACTION% -DskipTests=true %VAR_PROFILE% -f ./basis-apps
:: mvn clean %VAR_PROFILE% -DskipTests=true -f ./basis-apps
echo "===================================execute install END==================================================="
goto:eof


:full_install
set VAR_ACTION=install
set VAR_PROFILE=
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
echo DONE
exit 0