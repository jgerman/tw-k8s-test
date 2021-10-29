FROM java:8-alpine
RUN mkdir -p /app /app/resources
WORKDIR /app
COPY target/*-standalone.jar .
COPY resources/public resources/public
CMD java -jar tw-k8s-test-0.1.0-SNAPSHOT-standalone.jar
EXPOSE 3000
