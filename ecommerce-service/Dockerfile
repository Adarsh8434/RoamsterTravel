# ─── Stage 1: Build ───────────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Cache dependencies first (only re-run if pom.xml changes)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Build the jar
COPY src ./src
RUN mvn clean package -DskipTests -B

# ─── Stage 2: Runtime ─────────────────────────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine

# Security: run as non-root
RUN addgroup -S roamster && adduser -S roamster -G roamster
USER roamster

WORKDIR /app

# Copy jar from build stage (wildcard handles any version suffix)
COPY --from=builder /app/target/*.jar app.jar

# Expose the service port (overridable per service via ARG/ENV)
EXPOSE 8080

ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-jar", "app.jar"]
