@echo off

echo "1. 到basis-project, 执行flatten:flatten."
call  mvn -Dmaven.multiModuleProjectDirectory=basis-project flatten:clean flatten:flatten

echo "2. 到basis-spring-boot-starters, 执行flatten:flatten."
call mvn -Dmaven.multiModuleProjectDirectory=basis-spring-boot-starters flatten:clean flatten:flatten

echo "3. 到basis-project 执行mvn clear install.  打包core."
call mvn -Dmaven.multiModuleProjectDirectory=basis-project clean install -DskipTests=true

echo "4. 到basis-spring-boot-starters 执行mvn clear install. 打包starter."
call mvn -Dmaven.multiModuleProjectDirectory=basis-spring-boot-starters clean install -DskipTests=true

echo "5. 到basis-project 执行mvn clear install.  打包web-core."
call mvn -Dmaven.multiModuleProjectDirectory=basis-project clean install -DskipTests=true