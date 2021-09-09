@echo off

echo "1. 执行flatten:flatten."
call  mvn flatten:clean flatten:flatten

echo "2. 执行mvn clear install."
call mvn clean install -DskipTests=true