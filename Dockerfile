FROM gradle:7.5-jdk17-alpine as build

RUN apk --no-cache --update add \
        curl \
        wget \
        zip \
        unzip \
        bash \
        git \
        sed
RUN apk --no-cache --upgrade add grep
RUN apk update && apk add --no-cache docker-cli

# Set the current working directory inside the image
WORKDIR /app

# Copy the Gradle Wrapper executable and other necessary files
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Copy the source code
COPY src src

# Ensure Gradle Wrapper is executable
RUN chmod +x ./gradlew

# Download and cache dependencies, and build the project
RUN ./gradlew build --no-daemon

## Run stage
#FROM openjdk:17-alpine
#
## Copy the built JAR from the build stage
#COPY --from=build /app/build/libs/*.jar /app/app.jar
RUN chmod -R 777 /app/build/libs/doctor-api.jar
# Run the application
ENTRYPOINT ["java", "-jar", "/app/build/libs/doctor-api.jar"]