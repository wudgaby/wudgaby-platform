@echo off

echo "===================================deploy BEGIN==================================================="
cd /d ./basis-project
call mvn clean deploy -P release-sonatype -DskipTests=true

: pause

cd /d ../basis-spring-boot-starters
call mvn clean deploy -P release-sonatype -DskipTests=true
echo "===================================deploy END==================================================="