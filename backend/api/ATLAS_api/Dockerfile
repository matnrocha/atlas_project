FROM maven:latest
WORKDIR /ATLAS_API
COPY . .

RUN mvn install -DskipTests

CMD mvn spring-boot:run
