# ---------- STAGE 1: BUILD ----------
FROM gradle:9.3.1-jdk-25-and-25 AS build

WORKDIR /app

COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle

RUN chmod +x gradlew

RUN ./gradlew dependencies --no-daemon

COPY . .

RUN chmod +x gradlew

RUN ./gradlew clean build -x test --no-daemon


# ---------- STAGE 2: RUNTIME ----------
FROM eclipse-temurin:25-jre

WORKDIR /app

RUN useradd -m appuser

ARG APPLICATION_PORT=8080
ENV APPLICATION_PORT=${APPLICATION_PORT}

COPY --from=build /app/build/libs/*.jar app.jar

RUN chown appuser:appuser app.jar

USER appuser

EXPOSE 8080

ENV JAVA_OPTS="-XX:+UseContainerSupport \
               -XX:MaxRAMPercentage=75.0 \
               -XX:+ExitOnOutOfMemoryError \
               -XX:+UseG1GC \
               -Dfile.encoding=UTF-8"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
