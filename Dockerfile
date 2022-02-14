FROM openjdk:11
EXPOSE 8080
ADD /build/libs/code-with-spring-0.0.1-SNAPSHOT.jar code-with-spring.jar
ENV profile=dev
ENTRYPOINT ["java","-jar","code-with-spring.jar"]