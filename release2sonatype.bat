@echo off

echo "===================================release BEGIN==================================================="
call mvn clean deploy -DskipTests=true -f ./basis-project -P release-sonatype
: pause
call mvn clean deploy -DskipTests=true -f ./basis-spring-boot-starters -P release-sonatype
echo "===================================release END==================================================="