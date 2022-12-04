FROM openjdk:11
COPY ./build/libs/Bot-1.0-standalone.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "/app.jar"]
