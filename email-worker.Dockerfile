FROM gradle:8.6.0-jdk21 AS build

WORKDIR /app

COPY . .
RUN gradle bootJar

FROM openjdk:21 AS RUN
ENV MQ_HOST=localhost
ENV MQ_PORT=5555
ENV MQ_USER=test
ENV MQ_PASSWORD=test
ENV SMTP_HOST=smtp.mail.ru
ENV SMTP_USERNAME=test
ENV SMTP_PORT=25
ENV SMTP_PASSWORD=test

COPY --from=build /app/email-worker/build/libs/email-worker-0.0.3-rolling.jar /

ENTRYPOINT java -jar email-worker-0.0.3-rolling.jar