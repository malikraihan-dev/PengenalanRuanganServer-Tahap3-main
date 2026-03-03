FROM  maven:3.8.6-openjdk-8 as maven-builder
COPY src /app/src
COPY pom.xml /app
RUN mvn -f /app/pom.xml clean package -DskipTests


FROM openjdk:17-alpine

COPY --from=maven-builder app/target/samplecrud-0.0.1-SNAPSHOT.jar /app-service/samplecrud-0.0.1-SNAPSHOT.jar
RUN apk add --no-cache msttcorefonts-installer fontconfig
RUN update-ms-fonts

RUN ln -sf /usr/share/zoneinfo/Asia/Jakarta /etc/localtime

COPY *.xml /app-service/
COPY RunKey.key /app-service/
COPY application.properties /app-service/

WORKDIR /app-service

EXPOSE 8080
ENTRYPOINT ["java","-jar","samplecrud-0.0.1-SNAPSHOT.jar"]
