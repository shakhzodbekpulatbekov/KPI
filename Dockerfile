FROM openjdk:8-jdk-alpine
EXPOSE 7744
ADD target/KPI.jar KPI.jar
ENTRYPOINT ["java","-jar","KPI.jar"]