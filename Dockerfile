# ============================================================
# Stage 1: Build — Maven + JDK 21
# ============================================================
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# Cache de dependencias Maven antes de copiar el código fuente
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw dependency:go-offline -q

# Copiar fuentes y construir el JAR (modo fast-jar de Quarkus)
COPY src ./src
RUN ./mvnw package -DskipTests -q

# ============================================================
# Stage 2: Runtime — solo JRE 21 (imagen mínima)
# ============================================================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar el artefacto fast-jar producido por Quarkus
COPY --from=build /app/target/quarkus-app/lib/           ./lib/
COPY --from=build /app/target/quarkus-app/*.jar          ./
COPY --from=build /app/target/quarkus-app/app/           ./app/
COPY --from=build /app/target/quarkus-app/quarkus/       ./quarkus/

# Usuario sin privilegios para mayor seguridad
RUN addgroup -S nova && adduser -S nova -G nova
USER nova

EXPOSE 8080

# Las claves PEM se inyectan como variables de entorno en Render
# (ver .env.render y la guía de despliegue)
ENTRYPOINT ["java", \
  "-Dquarkus.http.host=0.0.0.0", \
  "-Djava.util.logging.manager=org.jboss.logmanager.LogManager", \
  "-jar", "quarkus-run.jar"]
