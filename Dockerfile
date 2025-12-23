FROM gradle:8.2.1-jdk17 AS builder

WORKDIR /app

# Gradle 캐시 (컨테이너 내부 캐시 유지)
ENV GRADLE_USER_HOME=/home/gradle/.gradle

# 의존성 파일만 먼저 복사 (캐시 핵심)
COPY build.gradle settings.gradle gradlew ./
COPY gradle ./gradle

# 의존성만 먼저 다운로드
RUN gradle --no-daemon dependencies

# 소스 복사
COPY src ./src

# 빌드 (clean, info 제거)
RUN gradle --no-daemon build -x test


FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
