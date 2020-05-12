package com.citi.WeatherAlarmDB.Services;

import com.citi.Exceptions.CustomerNotFoundException;
import com.citi.Exceptions.WrongAlarmIdException;
import com.citi.WeatherAlarmDB.Managers.WeatherReadManager;
import com.citi.WeatherAlarmDB.Models.Alarm;
import com.citi.WeatherAlarmDB.Models.Customer;
import com.citi.WeatherAlarmDB.Models.SetAlarm;
import com.citi.WeatherAlarmDB.Models.WeatherRead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.ServiceUnavailableException;

@Service
public class WeatherReadService {
    @Autowired
    private WeatherReadManager weatherReadManager;

    public WeatherRead getWeatherRead(String uuid, double latitude, double longitude) throws CustomerNotFoundException,
            ServiceUnavailableException, IllegalArgumentException {
        Customer customer = weatherReadManager.getCustomerByUuid(uuid);

        return weatherReadManager.getWeatherRead(latitude, longitude, customer);
    }

    public SetAlarm setAlarm(String uuid, long alarmId, double latitude, double longitude) throws CustomerNotFoundException, ServiceUnavailableException,
            WrongAlarmIdException {
        Customer customer = weatherReadManager.getCustomerByUuid(uuid);
        Alarm alarm = weatherReadManager.getAlarmByCustomerAndId(customer, alarmId);
        WeatherRead weatherRead = weatherReadManager.getWeatherRead(latitude, longitude, customer);

        SetAlarm setAlarm = new SetAlarm();

        if (!weatherReadManager.checkDayOfTheWeek(alarm)) {
            setAlarm.setHasToBeSet(false);
            return setAlarm;
        }

        if (latitude < 0 || longitude < 0) {
            setAlarm.setHasToBeSet(true);
        } else {
            weatherReadManager.setAlarmTimeBasedOnWeather(alarm, setAlarm, weatherRead);
        }

        setAlarm.setWeather(weatherRead.getWeather().getWeather());
        setAlarm.setWeatherTag(weatherRead.getWeatherTag());
        setAlarm.setDegrees(weatherRead.getDegrees());

        weatherReadManager.disableAlarm(setAlarm.isHasToBeSet(), alarm);

        return setAlarm;
    }
}