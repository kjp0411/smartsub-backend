FROM gradle:8.2.1-jdk17 AS builder

WORKDIR /app

# Gradle 캐시 위치를 변경해 충돌 방지
ENV GRADLE_USER_HOME=/tmp/gradle-cache

COPY . .

RUN gradle --no-daemon clean build -x test --info

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
