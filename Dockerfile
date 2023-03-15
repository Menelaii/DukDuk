FROM openjdk:17
ADD /build/libs/DukDuk-0.0.1-SNAPSHOT.jar DukDuk.jar
ENTRYPOINT ["java","-jar","DukDuk.jar"]