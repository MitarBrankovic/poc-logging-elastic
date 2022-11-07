FROM openjdk:11
VOLUME /tmp
COPY /target/consumer-0.0.1-SNAPSHOT.jar app.jar
COPY /scripts/wait-for-it.sh wait-for-it.sh
EXPOSE 8888
ENTRYPOINT ["java","-jar","/app.jar"]