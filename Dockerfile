# --- 1. 빌드 단계 ---
# Gradle과 JDK를 포함한 이미지를 사용하여 애플리케이션을 빌드합니다.
FROM openjdk:21-jdk-slim as builder
WORKDIR /app
COPY . .
RUN ./gradlew bootJar

# --- 2. 실행 단계 ---
FROM openjdk:21-jdk-slim
WORKDIR /app

# 빌드 단계에서 생성된 JAR 파일을 실행 단계 이미지로 복사합니다.
COPY --from=builder /app/build/libs/*.jar ./application.jar

# 컨테이너 실행 시 JAR 파일을 실행하는 명령어를 지정합니다.
ENTRYPOINT ["java", "-jar", "application.jar"]