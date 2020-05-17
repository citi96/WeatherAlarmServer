package com.citi.WeatherAlarmDB.Managers;

import com.citi.Exceptions.CustomerNotFoundException;
import com.citi.Exceptions.CustomerNotPremiumException;
import com.citi.Exceptions.WrongAlarmIdException;
import com.citi.WeatherAlarmDB.Models.Alarm;
import com.citi.WeatherAlarmDB.Models.Customer;
import com.citi.WeatherAlarmDB.Models.WeatherCondition;
import com.citi.WeatherAlarmDB.repositories.AlarmRepository;
import com.citi.WeatherAlarmDB.repositories.CustomerRepository;
import com.citi.WeatherAlarmDB.repositories.WeatherConditionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class AlarmManager {
    @Autowired
    private AlarmRepository alarmRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private WeatherConditionRepository weatherConditionRepository;


    public Customer getCustomerByUuid(String uuid) throws CustomerNotFoundException {
        Optional<Customer> customer = customerRepository.findById(uuid);

        if (customer.isEmpty()) {
            throw new CustomerNotFoundException(uuid);
        }

        return customer.get();
    }

    public Alarm addAlarm(Alarm alarm) throws CustomerNotPremiumException {
        if (!alarm.getCustomer().getIsPremium() && findAlarmsByCustomer(alarm.getCustomer()).size() >= 8) {
            throw new CustomerNotPremiumException("Cannot add another alarm for this customer.");
        }

        if (alarm.getAlarmTime() == null && alarm.getWeatherAlarmTime() == null) {
            throw new IllegalArgumentException("Alarm time: " + alarm.getAlarmTime() + ", Weather alarm time: " +
                    alarm.getWeatherAlarmTime() + " cannot be both null");
        }

        if (alarm.getWeatherAlarmTime() != null && alarm.getWeatherConditions().size() == 0) {
            throw new IllegalArgumentException(alarm.getWeatherConditions().toString());
        }

        return alarmRepository.save(alarm);
    }

    public List<Alarm> findAlarmsByCustomer(Customer customer) {
        return alarmRepository.findAlarmsByCustomer(customer);
    }

    public Alarm editAlarm(Alarm alarm, String alarmName, LocalTime alarmTime, LocalTime weatherAlarmTime, String[] weatherConditions,
                           String days, String ringtone, String ringtoneUri, boolean vibrate, boolean isEnabled) throws IllegalArgumentException {
        alarm.setName(alarmName);
        alarm.setAlarmTime(alarmTime);
        alarm.setWeatherAlarmTime(weatherAlarmTime);
        alarm.setWeatherConditions(getWeatherCondition(weatherConditions));
        alarm.setDays(days);
        alarm.setRingtone(ringtone);
        alarm.setRingtoneUri(ringtoneUri);
        alarm.setVibrate(vibrate);
        alarm.setEnabled(isEnabled);

        return alarmRepository.save(alarm);
    }

    public Alarm getAlarmByCustomerAndId(Customer customer, long alarmId) throws WrongAlarmIdException {
        Optional<Alarm> alarm = alarmRepository.findById(alarmId);
        if (alarm.isEmpty() || !alarm.get().getCustomer().equals(customer)) {
            throw new WrongAlarmIdException();
        }

        return alarm.get();
    }

    public List<WeatherCondition> getWeatherCondition(String[] weatherConditions) throws IllegalArgumentException {
        if (weatherConditions == null) {
            return null;
        }

        List<WeatherCondition> weatherConditionList = new LinkedList<>();

        for (String weatherCondition : weatherConditions) {
            weatherConditionRepository.findById(weatherCondition).ifPresentOrElse(weatherConditionList::add, () -> {
                throw new IllegalArgumentException(weatherCondition);
            });
        }

        return weatherConditionList;
    }

    public void deleteAlarm(Alarm alarm) {
        alarmRepository.delete(alarm);
    }
}
