package com.citi.WeatherAlarmDB.Managers;

import com.citi.Exceptions.CustomerNotFoundException;
import com.citi.Exceptions.WrongAlarmIdException;
import com.citi.WeatherAlarmDB.Models.*;
import com.citi.WeatherAlarmDB.repositories.AlarmRepository;
import com.citi.WeatherAlarmDB.repositories.CustomerRepository;
import com.citi.WeatherAlarmDB.repositories.WeatherConditionRepository;
import com.citi.WeatherAlarmDB.repositories.WeatherReadRepository;
import okhttp3.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.ServiceUnavailableException;
import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

@Service
public class WeatherReadManager {
    @Autowired
    private WeatherReadRepository weatherReadRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private WeatherConditionRepository weatherConditionRepository;
    @Autowired
    private AlarmRepository alarmRepository;

    private final String[] DAYS_OF_THE_WEEK = {"sun", "mon", "tue", "wed", "thu", "fri", "sat", "sun"};

    public Customer getCustomerByUuid(String uuid) throws CustomerNotFoundException {
        Optional<Customer> customer = customerRepository.findById(uuid);

        if (customer.isEmpty()) {
            throw new CustomerNotFoundException(uuid);
        }

        return customer.get();
    }

    public WeatherRead getWeatherRead(double latitude, double longitude, Customer customer) throws ServiceUnavailableException {
        WeatherRead weatherRead = getLastWeatherReadByCustomer(customer);

        if (weatherRead == null || isNewReadRequired(customer, weatherRead)) {
            weatherRead = getNewWeatherRead(customer, latitude, longitude);
        }
        return weatherRead;
    }

    private WeatherRead getLastWeatherReadByCustomer(Customer customer) {
        List<WeatherRead> lastWeatherReads = weatherReadRepository.findWeatherReadByCustomer(customer);

        if (lastWeatherReads.size() == 0) {
            return null;
        }

        WeatherRead lastWeatherRead = lastWeatherReads.get(0);
        for (WeatherRead weatherRead : lastWeatherReads) {
            if (lastWeatherRead.getTime().before(weatherRead.getTime())) {
                lastWeatherRead = weatherRead;
            }
        }

        return lastWeatherRead;
    }

    private boolean isNewReadRequired(Customer customer, WeatherRead weatherRead) {
        Date today = new Date();
        return today.getTime() > weatherRead.getTime().getTime() + customer.getMinuteBeforeNextRead() * 60 * 1000;
    }

    private WeatherRead getNewWeatherRead(Customer customer, double latitude, double longitude) throws ServiceUnavailableException, IllegalArgumentException {
        Map<String, String> weatherApiResponseParams = executeWeatherApiCall(latitude, longitude);
        WeatherRead weatherRead = new WeatherRead();
        weatherRead.setCustomer(customer);
        weatherRead.setWeather(getCorrectWeatherIcon(weatherApiResponseParams.get("icon")));
        weatherRead.setWeatherTag(weatherApiResponseParams.get("summary"));
        weatherRead.setDegrees(Math.round(Float.parseFloat(weatherApiResponseParams.get("temperature"))));

        return weatherReadRepository.save(weatherRead);
    }

    private WeatherCondition getCorrectWeatherIcon(String icon) {
        if (!icon.contains("day") && !icon.contains("night")) {
            int hour = GregorianCalendar.getInstance().get(GregorianCalendar.HOUR_OF_DAY);
            if (hour > 20 || hour < 6) {
                icon += "-night";
            } else {
                icon += "-day";
            }
        }
        Optional<WeatherCondition> weatherCondition = weatherConditionRepository.findById(icon);
        if (weatherCondition.isEmpty()) {
            throw new IllegalArgumentException(icon);
        }
        return weatherCondition.get();
    }

    private Map<String, String> executeWeatherApiCall(double latitude, double longitude) throws ServiceUnavailableException {
        OkHttpClient client = new OkHttpClient();

        String url = "https://api.darksky.net/forecast/9bd2818e4aa597da5959d5795bdbf9c2/" + latitude + "," + longitude
                + "?units=si&exclude=daily&exclude=hourly&exclude=flags&exclude=minutely";
        Request request = new Request.Builder().url(url).get().build();

        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new ServiceUnavailableException();
            }

            return handleResponse(response.body().string());
        } catch (IOException e) {
            Logger.getAnonymousLogger().severe(e.getMessage());
            e.printStackTrace();
            throw new ServiceUnavailableException();
        }
    }

    private Map<String, String> handleResponse(String responseBody) {
        JSONObject json = new JSONObject(responseBody);
        Map<String, String> requestedParams = new HashMap<>();

        requestedParams.put("summary", json.getJSONObject("currently").getString("summary"));
        requestedParams.put("icon", json.getJSONObject("currently").getString("icon"));
        requestedParams.put("temperature", json.getJSONObject("currently").getFloat("temperature") + "");

        return requestedParams;
    }

    public Alarm getAlarmByCustomerAndId(Customer customer, long alarmId) throws WrongAlarmIdException {
        Optional<Alarm> alarm = alarmRepository.findById(alarmId);
        if (alarm.isEmpty() || !alarm.get().getCustomer().equals(customer)) {
            throw new WrongAlarmIdException();
        }

        return alarm.get();
    }

    public void setAlarmTimeBasedOnWeather(Alarm alarm, SetAlarm setAlarm, WeatherRead weatherRead) {
        if (alarm.getWeatherConditions() == null || (alarm.getAlarmTime() == null && alarm.getWeatherConditions().contains(weatherRead.getWeather()))
                || (alarm.getWeatherAlarmTime().compareTo(alarm.getAlarmTime()) < 0 && alarm.getWeatherConditions().contains(weatherRead.getWeather()))
                || alarm.getAlarmTime().compareTo(alarm.getWeatherAlarmTime()) < 0) {
            setAlarm.setHasToBeSet(true);
            setAlarm.setOtherTime(null);
        } else if (alarm.getWeatherAlarmTime().compareTo(alarm.getAlarmTime()) < 0  && !alarm.getWeatherConditions().contains(weatherRead.getWeather())) {
            setAlarm.setHasToBeSet(true);
            setAlarm.setOtherTime(alarm.getAlarmTime());
        } else {
            setAlarm.setHasToBeSet(false);
            setAlarm.setOtherTime(null);
        }
    }

    public boolean checkDayOfTheWeek(Alarm alarm) {
        Date today = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(today);

        return (alarm.getDays().equals("Everyday") || alarm.getDays().equals("Only once")) ||
                alarm.getDays().toLowerCase().contains(DAYS_OF_THE_WEEK[calendar.get(Calendar.DAY_OF_WEEK)]);
    }

    public void disableAlarm(boolean alarmSet, Alarm alarm) {
        if (alarmSet && alarm.getDays().equals("Only once")) {
            alarm.setEnabled(false);
            alarmRepository.save(alarm);
        }
    }
}
