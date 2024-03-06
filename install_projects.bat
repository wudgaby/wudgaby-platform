@echo off
rem 解决中文乱码
chcp 65001

set VAR_ACTION=install
set VAR_PROFILE=


:main
echo 1.flatten
echo 2.install
echo 3.full install
echo 4.release to sonatype
echo 5.release basis-apps to sonatype
echo 6.exit

set /p opt=Enter your option:

if %opt% == 1 call:flatten
if %opt% == 2 call:install
if %opt% == 3 goto flatten_install
if %opt% == 4 goto release2sonatype
if %opt% == 5 goto releaseapps2sonatype
if %opt% == 6 goto end

echo Invalid Option
goto main

:flatten
echo "===================================execute flatten BEGIN==================================================="
call  mvn flatten:clean flatten:flatten
echo "===================================execute flatten END==================================================="
goto:eof

:install
echo "===================================execute install BEGIN==================================================="
echo "ACTION:%VAR_ACTION%"
echo "PROFILE:%VAR_PROFILE%"

call mvn clean
call mvn %VAR_ACTION% -DskipTests=true %VAR_PROFILE% -ff -pl basis-project/basis-project-web-core -am
call mvn %VAR_ACTION% -DskipTests=true %VAR_PROFILE% -ff -f ./basis-spring-boot-starters
call mvn %VAR_ACTION% -DskipTests=true %VAR_PROFILE% -ff -f ./basis-project-dependencies
if %VAR_ACTION% == install (
  call mvn %VAR_ACTION% -DskipTests=true %VAR_PROFILE% -ff -f ./basis-apps
)
echo "===================================execute install END==================================================="
goto:eof

:apps_install
echo "===================================execute apps install BEGIN==================================================="
echo "ACTION:%VAR_ACTION%"
echo "PROFILE:%VAR_PROFILE%"

call mvn clean
call mvn %VAR_ACTION% -DskipTests=true %VAR_PROFILE% -ff -f ./basis-apps
echo "===================================execute apps install END==================================================="
goto:eof


:flatten_install
set VAR_ACTION=install
set VAR_PROFILE=
call:flatten
call:install
goto end

:release2sonatype
set VAR_ACTION=deploy
set VAR_PROFILE=-P release-sonatype
call:flatten
call:install
goto end

releaseapps2sonatype
set VAR_ACTION=deploy
set VAR_PROFILE=-P release-sonatype
call:flatten
call:apps_install
goto end

:end
echo DONE
exit 0