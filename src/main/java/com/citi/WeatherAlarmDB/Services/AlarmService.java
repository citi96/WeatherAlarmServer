package com.citi.WeatherAlarmDB.Services;

import com.citi.Exceptions.CustomerNotFoundException;
import com.citi.Exceptions.CustomerNotPremiumException;
import com.citi.Exceptions.WrongAlarmIdException;
import com.citi.WeatherAlarmDB.Managers.AlarmManager;
import com.citi.WeatherAlarmDB.Models.Alarm;
import com.citi.WeatherAlarmDB.Models.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AlarmService {
    @Autowired
    private AlarmManager alarmManager;

    public Alarm addAlarm(String alarmName, String uuid, Date alarmTime, Date weatherAlarmTime, String[] weatherConditions,
                          String days, String ringtone, String ringtoneUri, boolean vibrate)
            throws CustomerNotFoundException, CustomerNotPremiumException, IllegalArgumentException {
        Customer customer = alarmManager.getCustomerByUuid(uuid);

        Alarm alarm = new Alarm();
        alarm.setName(alarmName);
        alarm.setAlarmTime(alarmTime);
        alarm.setCustomer(customer);
        alarm.setDays(days);
        alarm.setWeatherAlarmTime(weatherAlarmTime);
        alarm.setWeatherConditions(alarmManager.getWeatherCondition(weatherConditions));
        alarm.setRingtone(ringtone);
        alarm.setRingtoneUri(ringtoneUri);
        alarm.setVibrate(vibrate);

        return alarmManager.addAlarm(alarm);
    }

    public List<Alarm> getAlarms(String uuid) throws CustomerNotFoundException {
        Customer customer = alarmManager.getCustomerByUuid(uuid);

        return alarmManager.findAlarmsByCustomer(customer);
    }

    public Alarm editAlarm(String uuid, long alarmId, String alarmName, Date alarmTime, Date weatherAlarmTime, String[] weatherConditions,
                           String days, String ringtone, String ringtoneUri, boolean vibrate, boolean isEnabled)
            throws CustomerNotFoundException, WrongAlarmIdException {
        Customer customer = alarmManager.getCustomerByUuid(uuid);
        Alarm alarm = alarmManager.getAlarmByCustomerAndId(customer, alarmId);

        return alarmManager.editAlarm(alarm, alarmName, alarmTime, weatherAlarmTime, weatherConditions, days, ringtone, ringtoneUri, vibrate, isEnabled);
    }
}