FROM openjdk:17
LABEL authors="sanju"

COPY target/bonafidegenerator.jar /bonafidegenerator.jar

ENTRYPOINT ["java", "-jar", "/bonafidegenerator.jar"]
