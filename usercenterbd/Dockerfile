# 基础镜像
 FROM eclipse-temurin:8u402-b06-jdk-alpine

# 复制 JAR 文件到容器中
 COPY usercenterbd-0.0.1-SNAPSHOT.jar /app/usercenterbd.jar

# # 运行应用
CMD ["java", "-Xms1024m", "-Xmx2048m", "-jar", "/app/usercenterbd.jar","--spring.profiles.active=prod"]