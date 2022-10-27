FROM openjdk:11
EXPOSE 7744
ADD target/loretto.jar loretto.jar
ENTRYPOINT ["java","-jar","loretto.jar"]
