@echo off

echo "===================================execute flatten BEGIN==================================================="
call  mvn flatten:clean -f ./basis-parent
call  mvn flatten:clean -f ./basis-project
call  mvn flatten:clean -f ./basis-spring-boot-starters
call  mvn flatten:clean -f ./basis-apps
echo "===================================execute flatten END==================================================="

: pause

echo "===================================execute flatten BEGIN==================================================="
call  mvn flatten:clean flatten:flatten -f ./basis-parent
call  mvn flatten:clean flatten:flatten -f ./basis-project
call  mvn flatten:clean flatten:flatten -f ./basis-spring-boot-starters
call  mvn flatten:clean flatten:flatten -f ./basis-apps
echo "===================================execute flatten END==================================================="

echo "===================================execute install BEGIN==================================================="
call mvn clean install -DskipTests=true -f ./basis-parent -P gpg,release-sonatype
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