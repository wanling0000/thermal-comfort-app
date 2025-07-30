# Maven 构建阶段
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

# 运行阶段
FROM eclipse-temurin:17-jre
WORKDIR /app

# 拷贝 app 模块 jar
COPY --from=build /build/thermal-comfort-app-app/target/thermal-comfort-app.jar app.jar

# 设置时区
ENV TZ=Europe/Dublin
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

EXPOSE 8091
ENTRYPOINT ["java", "-jar", "app.jar"]
