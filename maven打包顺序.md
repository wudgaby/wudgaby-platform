maven顺序
1. 到basis-project, 执行flatten:flatten.
2. 到basis-spring-boot-starters, 执行flatten:flatten.
3. 到basis-project 执行mvn clear install.  打包core.
4. 到basis-spring-boot-starters 执行mvn clear install. 打包starter.
5. 到basis-project 执行mvn clear install.  打包web-core.