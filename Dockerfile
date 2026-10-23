# ---------- Etapa 1: build con Gradle (usa el wrapper del proyecto) ----------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Copiamos primero solo lo necesario para resolver dependencias: así Docker
# cachea esta capa y no vuelve a descargar todo cada vez que cambia el código.
COPY gradlew gradlew.bat ./
COPY gradle ./gradle
COPY build.gradle settings.gradle ./
RUN chmod +x gradlew
RUN ./gradlew dependencies --no-daemon || true

# Ahora sí el código fuente, y se compila el jar ejecutable.
COPY src ./src
RUN ./gradlew bootJar -x test --no-daemon

# ---------- Etapa 2: imagen de ejecución, liviana y sin herramientas de build ----------
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

# Usuario sin privilegios: buena práctica, el proceso no corre como root.
RUN addgroup -S spring && adduser -S spring -G spring
COPY --from=build /app/build/libs/*.jar app.jar
RUN chown spring:spring app.jar
USER spring

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
