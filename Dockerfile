FROM gradle:8.6.0-jdk21 AS build

WORKDIR /app

COPY . .
RUN gradle bootJar

FROM openjdk:21
ARG jarFileName=business1-0.0.1-rolling.jar

COPY --from=build /app/build/libs/$jarFileName /

CMD java -jar $jarFileName