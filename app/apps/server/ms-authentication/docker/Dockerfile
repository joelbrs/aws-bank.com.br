FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src
COPY .mvn .mvn
COPY mvnw ./
COPY .env .env
RUN ./mvnw dependency:go-offline

RUN ./mvnw clean package -DskipTests 

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]