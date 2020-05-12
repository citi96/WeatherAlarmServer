package com.citi.WeatherAlarmDB.Models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class WeatherCondition {
    @Id
    @Column
    private String weather;

    public WeatherCondition() {
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }
}
