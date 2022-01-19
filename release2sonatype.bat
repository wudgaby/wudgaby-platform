@echo off

echo "===================================release BEGIN==================================================="
call mvn clean deploy -DskipTests=true -P release-sonatype -N

call mvn clean deploy -DskipTests=true -f ./basis-project -P release-sonatype
call mvn clean deploy -DskipTests=true -f ./basis-spring-boot-starters -P release-sonatype
call mvn clean deploy -DskipTests=true -f ./basis-project-dependencies -P release-sonatype
echo "===================================release END==================================================="