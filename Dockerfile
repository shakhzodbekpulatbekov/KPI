FROM openjdk:11
EXPOSE 7744
ADD target/KPI.jar KPI.jar
ENTRYPOINT ["java","-jar","KPI.jar"]
