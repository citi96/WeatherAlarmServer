package com.citi.WeatherAlarmDB.Controllers;

import com.citi.Exceptions.CustomerNotFoundException;
import com.citi.Exceptions.WrongAlarmIdException;
import com.citi.WeatherAlarmDB.Models.SetAlarm;
import com.citi.WeatherAlarmDB.Models.WeatherRead;
import com.citi.WeatherAlarmDB.Services.WeatherReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.naming.ServiceUnavailableException;
import java.util.logging.Logger;

@Controller
public class WeatherReadController {
    @Autowired
    WeatherReadService weatherReadService;

    @GetMapping("/weatherRead")
    ResponseEntity<WeatherRead> getWeatherRead(String uuid, double latitude, double longitude) {
        WeatherRead weatherRead;
        try {
            weatherRead = weatherReadService.getWeatherRead(uuid, latitude, longitude);
        } catch (CustomerNotFoundException e) {
            Logger.getAnonymousLogger().severe(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (ServiceUnavailableException e) {
            Logger.getAnonymousLogger().severe(e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        } catch (IllegalArgumentException e) {
            Logger.getAnonymousLogger().severe(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.ok(weatherRead);
    }

    @GetMapping("/setAlarm")
    ResponseEntity<SetAlarm> setAlarm(String uuid, long alarmId, double latitude, double longitude) {
        SetAlarm setAlarm;
        try {
            setAlarm = weatherReadService.setAlarm(uuid, alarmId, latitude, longitude);
        } catch (ServiceUnavailableException e) {
            Logger.getAnonymousLogger().severe(e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        } catch (CustomerNotFoundException e) {
            Logger.getAnonymousLogger().severe(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (WrongAlarmIdException e) {
            Logger.getAnonymousLogger().severe(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(setAlarm);
    }
}
