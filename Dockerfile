# 1. 사용할 베이스 이미지 선택 (Java 17 환경)
FROM openjdk:17-jdk-slim

# 2. Gradle 빌드 후 생성된 JAR 파일을 컨테이너 내부로 복사
#    build/libs/ 경로에 생성된 *.jar 파일을 'app.jar' 이름으로 복사
COPY build/libs/*.jar app.jar

# 3. 컨테이너에서 열어둘 포트 (Spring Boot 기본 8080)
EXPOSE 8080

# 4. 컨테이너 실행 시 스프링부트 JAR을 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
