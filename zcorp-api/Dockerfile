FROM openjdk:8-jre

EXPOSE 3000

COPY /build/libs/zcorp-api.jar zcorp-api.jar
CMD ["java", "-Xmx512m", "-jar", "zcorp-api.jar"]