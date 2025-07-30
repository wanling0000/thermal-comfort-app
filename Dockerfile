# 1. 构建阶段：用 Maven 构建整个项目
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

# 2. 运行阶段：只保留主应用的 jar
FROM eclipse-temurin:17-jre
WORKDIR /app

# 复制 thermal-comfort-app-app 模块构建的 jar
COPY --from=build /build/thermal-comfort-app-app/target/thermal-comfort-app-app-app.jar app.jar

# 设置爱尔兰时区
ENV TZ=Europe/Dublin
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

EXPOSE 8091

ENTRYPOINT ["java", "-jar", "app.jar"]
