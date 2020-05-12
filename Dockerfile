FROM openjdk:11
COPY ./target/classes/com/citi/WeatherAlarmDB/ /tmp
WORKDIR /temp
ENTRYPOINT ["java", "WeatherAlarmDbApplication"]
