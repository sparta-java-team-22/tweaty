# 베이스 이미지로 openjdk를 사용
FROM openjdk:17-jdk-slim

# 애플리케이션의 빌드를 위한 워킹 디렉토리 설정
WORKDIR /app

# 빌드된 JAR 파일을 Docker 이미지에 복사
COPY build/libs/*.jar app.jar

# 애플리케이션을 실행하는 명령
CMD ["java", "-jar", "app.jar"]
