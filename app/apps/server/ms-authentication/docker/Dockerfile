FROM eclipse-temurin:21-jdk-jammy AS build

WORKDIR /app

COPY . .

RUN ./application/mvnw -f pom.xml dependency:go-offline
RUN ./application/mvnw -f pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring

COPY .env .env
COPY --from=build /app/application/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "if [ -f .env ]; then export $(grep -v '^#' .env | xargs); fi; exec java -jar app.jar"]