# 基础镜像使用java
FROM java:8

# 作者
MAINTAINER wudgaby <wudgaby@sina.com>

# VOLUME 指定了临时文件目录为/tmp。
# 其效果是在主机 /var/lib/docker 目录下创建了一个临时文件，并链接到容器的/tmp
VOLUME /tmp

#EXPOSE命令只是声明了容器应该打开的端口并没有实际上将它打开!也就是说，如果你不用-p或者-P中指定要映射的端口，你的容器是不会映射端口出去的
#写在Dockerfile中进行声明，能让运维人员或者后来者知道我们开启了容器的哪些端口
#当我们声明了EXPOSE端口之后，我们使用-P命令进行随机映射的时候，是会对这个端口进行映射的
EXPOSE 8010

# 将jar包添加到容器中并更名为app.jar
ADD sysweb*.jar app.jar

# 运行jar包
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS="-server -Xms512m -Xmx512m -XX:-TieredCompilation -XX:+UseG1GC -XX:+UseStringDeduplication -Djava.security.egd=file:/dev/./urandom -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/data/logs/"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app.jar", "-Dspring.profiles.active=dev"]