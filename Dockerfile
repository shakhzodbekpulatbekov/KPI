FROM openjdk:11
EXPOSE 7744
ADD target/kpi.jar currency.jar
ENTRYPOINT ["java","-jar","currency.jar"]
