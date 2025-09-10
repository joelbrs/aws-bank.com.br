FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /app

COPY . .

RUN ./application/mvnw -f pom.xml dependency:go-offline
RUN ./application/mvnw -f pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring

COPY --from=build /app/application/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]