FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} WeatherAlarmServer.jar
WORKDIR /temp
ENTRYPOINT ["java", "-jar", "/WeatherAlarmServer.jar"]
