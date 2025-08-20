# ---- Build stage ----
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
# Copy parent pom/apis and service files
COPY ../../pom.xml ./pom.xml
COPY ../../apis ./apis
COPY . .
# Build only this module;
RUN mvn -q -DskipTests movies-service:package

# ---- Run stage ----
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
