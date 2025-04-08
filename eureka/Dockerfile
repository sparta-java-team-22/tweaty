FROM eclipse-temurin:17-jdk

WORKDIR /eureka

# 프로젝트 소스 복사
COPY . .

# Gradle 실행 권한 부여
RUN chmod +x gradlew

# JAR 빌드 없이 애플리케이션 실행 (bootRun 모드)
CMD ["./gradlew", "bootRun"]