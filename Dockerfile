FROM eclipse-temurin:17-jre

COPY target/MS_DB.jar /app.jar

ENTRYPOINT ["java","-jar","/app.jar"]