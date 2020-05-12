package com.citi.WeatherAlarmDB.Models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table
public class Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private long alarmId;

    @Column
    private String name;

    @ManyToOne
    private Customer customer;

    @Column
    @DateTimeFormat(pattern = "HH:mm")
    private Date alarmTime;

    @Column
    @DateTimeFormat(pattern = "HH:mm")
    private Date weatherAlarmTime;

    @ManyToMany
    private List<WeatherCondition> weatherConditions;

    @Column
    private String days;

    @Column
    private String ringtone;

    @Column
    private String ringtoneUri;

    @Column
    private boolean vibrate;

    @Column
    private boolean isEnabled = true;

    public Alarm() {
    }

    public long getAlarmId() {
        return alarmId;
    }

    public void setAlarmId(long alarmId) {
        this.alarmId = alarmId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(Date alarmTime) {
        this.alarmTime = alarmTime;
    }

    public Date getWeatherAlarmTime() {
        return weatherAlarmTime;
    }

    public void setWeatherAlarmTime(Date weatherAlarmTime) {
        this.weatherAlarmTime = weatherAlarmTime;
    }

    public List<WeatherCondition> getWeatherConditions() {
        return weatherConditions;
    }

    public void setWeatherConditions(List<WeatherCondition> weatherConditions) {
        this.weatherConditions = weatherConditions;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getRingtone() {
        return ringtone;
    }

    public void setRingtone(String ringtone) {
        this.ringtone = ringtone;
    }

    public String getRingtoneUri() {
        return ringtoneUri;
    }

    public void setRingtoneUri(String ringtoneUri) {
        this.ringtoneUri = ringtoneUri;
    }

    public boolean isVibrate() {
        return vibrate;
    }

    public void setVibrate(boolean vibrate) {
        this.vibrate = vibrate;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }
}