FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /app

COPY application/pom.xml application/pom.xml
COPY application/.mvn application/.mvn
COPY application/mvnw application/mvnw
COPY application/src application/src
COPY domain domain

RUN ./application/mvnw -f application/pom.xml dependency:go-offline
RUN ./application/mvnw -f application/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring

COPY --from=build /app/application/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]