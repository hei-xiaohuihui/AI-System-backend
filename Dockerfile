# 基础镜像
FROM openjdk:17-jdk-alpine

# 指定工作目录
WORKDIR /app

# 复制jar文件到镜像中
COPY ./AI-System-0.0.1-SNAPSHOT.jar app.jar

# 开放端口
EXPOSE 7816

# 启动命令
ENTRYPOINT ["java","-jar","app.jar"]