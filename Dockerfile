# Build stage
FROM gradle:8.6-alpine AS BUILD_STAGE
WORKDIR /usr/app/
COPY . .
RUN gradle bootJar

# Package stage
FROM openjdk:17-alpine
ENV JAR_NAME=foodbank-backend-0.0.1-SNAPSHOT.jar
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME
COPY --from=BUILD_STAGE $APP_HOME .
EXPOSE 8080
ENTRYPOINT exec java -jar $APP_HOME/build/libs/$JAR_NAME