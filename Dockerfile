FROM eclipse-temurin:21-jre
WORKDIR /app
COPY build/libs/*.jar app.jar
COPY topology.json topology.json
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]