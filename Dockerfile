FROM eclipse-temurin:21-jdk as builder

WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

COPY src ./src

RUN ./mvnw clean package -DskipTests


FROM eclipse-temurin:21-jre

WORKDIR /app

RUN useradd --system --create-home springuser

COPY --from=builder /app/target/*.jar app.jar

RUN chown springuser:springuser app.jar

USER springuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]