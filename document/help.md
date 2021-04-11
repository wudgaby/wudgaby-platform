提交空目录

```
find . -type d -empty -exec touch {}/.gitignore \;
```


允许不相关的历史提交
```
git pull origin master --allow-unrelated-histories
```



出现错误的主要原因是github中的README.md文件不在本地代码目录中。
解决方法：

（1）可以通过如下命令进行代码合并【注：pull=fetch+merge】

            git pull --rebase origin master

（2）再执行语句：

         git push -u origin master
         
         
``
deploy:deploy-file -Dfile=pom.xml -DgroupId=com.wudgaby.platform -DartifactId=base-spring-boot-starters -Dversion=${revision} -Dpackaging=jar
``