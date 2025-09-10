FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /app

COPY application/pom.xml .
COPY application/.mvn .mvn
COPY application/mvnw ./
COPY application/src ./src

RUN ./mvnw dependency:go-offline
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]