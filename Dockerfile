###############################################################
# Dockerfile — Spring Boot (Multi-stage build)
# 빌드 이미지와 실행 이미지 분리 → 최종 이미지 경량화
###############################################################

# ── Stage 1: 빌드 ────────────────────────────────────────────
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# 의존성 캐시 레이어 (소스 변경 시 재다운로드 방지)
COPY gradlew build.gradle settings.gradle ./
COPY gradle ./gradle
RUN ./gradlew dependencies --no-daemon || true

COPY src ./src
RUN ./gradlew bootJar --no-daemon

# ── Stage 2: 실행 ────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# 보안: root가 아닌 전용 사용자로 실행
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

COPY --from=builder /app/build/libs/*.jar app.jar

# Spring Boot 포트
EXPOSE 8080

# Actuator 포트
EXPOSE 9090

# JVM 메모리 설정
  ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-jar", "app.jar"]
