# Build stage
FROM gradle:8.6-alpine AS BUILD_STAGE
WORKDIR /usr/app/

COPY dish-service ./dish-service
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .
COPY gradlew .
COPY gradlew.bat .

RUN gradle bootJar

# Package stage
FROM openjdk:17-alpine
ENV JAR_NAME=dish-service.jar
ENV APP_HOME=/usr/app
WORKDIR $APP_HOME
COPY --from=BUILD_STAGE $APP_HOME .
EXPOSE 8080
ENTRYPOINT exec java -jar $APP_HOME/dish-service/build/libs/$JAR_NAME
# ENTRYPOINT ["tail", "-f", "/dev/null"] # Keeps app running (useful for debugging)