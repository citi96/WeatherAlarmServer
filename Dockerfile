FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} WeatherAlarmServer.jar
EXPOSE 5005
WORKDIR /temp
ENTRYPOINT ["java", "-jar",  "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "/WeatherAlarmServer.jar"]
