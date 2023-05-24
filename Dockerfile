FROM openjdk:11
EXPOSE 7744
ADD target/kpi.jar kpi.jar
ENTRYPOINT ["java","-jar","kpi.jar"]
