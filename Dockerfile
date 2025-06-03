FROM maven as build

WORKDIR /comercio
COPY . .

RUN mvn clean package -Dspring.profiles.active=dev

FROM ghcr.io/graalvm/jdk-community:17

WORKDIR /comercio
COPY --from=build /comercio/target/ms-comercio-0.0.1-SNAPSHOT.jar ./app.jar

CMD java -jar app.jar