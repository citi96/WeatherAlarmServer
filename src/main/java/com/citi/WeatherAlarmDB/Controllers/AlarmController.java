package com.citi.WeatherAlarmDB.Controllers;

import com.citi.Exceptions.CustomerNotFoundException;
import com.citi.Exceptions.CustomerNotPremiumException;
import com.citi.Exceptions.WrongAlarmIdException;
import com.citi.WeatherAlarmDB.Models.Alarm;
import com.citi.WeatherAlarmDB.Services.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class AlarmController {
    @Autowired
    private AlarmService alarmService;

    @PostMapping("/addAlarm")
    public ResponseEntity<Alarm> addAlarm(@RequestParam String alarmName, String uuid, @DateTimeFormat(pattern = "HH:mm") Date alarmTime,
                                          @DateTimeFormat(pattern = "HH:mm") Date weatherAlarmTime, String[] weatherConditions, String days,
                                          String ringtone, String ringtoneUri, boolean vibrate) {
        Alarm alarm;
        try {
            alarm = alarmService.addAlarm(alarmName, uuid, alarmTime, weatherAlarmTime, weatherConditions, days, ringtone, ringtoneUri, vibrate);
        } catch (CustomerNotPremiumException e) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
        } catch (CustomerNotFoundException e) {
            return handleCustomerNotFoundException(e).build();
        } catch (IllegalArgumentException e){
            Logger.getAnonymousLogger().severe(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(alarm);
    }

    @GetMapping("/getAlarms")
    public ResponseEntity<List<Alarm>> getAlarms(@RequestParam String uuid) {
        List<Alarm> alarms;
        try {
            alarms = alarmService.getAlarms(uuid);
        } catch (CustomerNotFoundException e) {
            return handleCustomerNotFoundException(e).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(alarms);
    }

    private ResponseEntity.BodyBuilder handleCustomerNotFoundException(CustomerNotFoundException e) {
        Logger.getAnonymousLogger().severe(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED);
    }

    @PutMapping ("/editAlarm")
    public ResponseEntity<Alarm> editAlarm(@RequestParam long alarmId, String alarmName, String uuid, @DateTimeFormat(pattern = "HH:mm") Date alarmTime,
                                           @DateTimeFormat(pattern = "HH:mm") Date weatherAlarmTime, String[] weatherConditions, String days,
                                           String ringtone, String ringtoneUri, boolean vibrate, boolean isEnabled) {
        Alarm alarm = null;
        try {
            alarm = alarmService.editAlarm(uuid, alarmId, alarmName, alarmTime, weatherAlarmTime, weatherConditions, days, ringtone, ringtoneUri,
                    vibrate, isEnabled);
        } catch (CustomerNotFoundException e) {
            return handleCustomerNotFoundException(e).build();
        } catch (WrongAlarmIdException | IllegalArgumentException e) {
            Logger.getAnonymousLogger().severe(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return ResponseEntity.status(HttpStatus.OK).body(alarm);
    }
}
