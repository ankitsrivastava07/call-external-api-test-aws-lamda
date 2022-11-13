FROM openjdk:11
EXPOSE 8081
ADD target/one-instance-request-call-0.0.1-SNAPSHOT-shaded.jar one-instance-request-call-0.0.1-SNAPSHOT-shaded.jar
ENTRYPOINT ["java","-jar","/one-instance-request-call-0.0.1-SNAPSHOT-shaded.jar"]