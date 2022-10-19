FROM openjdk:11
EXPOSE 7744
ADD target/kpi.jar KPI.jar
ENTRYPOINT ["java","-jar","kpi.jar"]