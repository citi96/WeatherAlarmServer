package com.citi.WeatherAlarmDB.Models;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table
public class WeatherRead implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private long weatherReadId;

    @ManyToOne
    private Customer customer;

    @ManyToOne
    private WeatherCondition weather;

    @Column(updatable = false)
    private String weatherTag;

    @Column(updatable = false)
    private int degrees;

    @Column(insertable = false, updatable = false)
    @Generated(GenerationTime.INSERT)
    private Date time;

    public WeatherRead() {
    }

    public long getWeatherReadId() {
        return weatherReadId;
    }

    public void setWeatherReadId(long weatherReadId) {
        this.weatherReadId = weatherReadId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public WeatherCondition getWeather() {
        return weather;
    }

    public void setWeather(WeatherCondition weather) {
        this.weather = weather;
    }

    public String getWeatherTag() {
        return weatherTag;
    }

    public void setWeatherTag(String weatherTag) {
        this.weatherTag = weatherTag;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getDegrees() {
        return degrees;
    }

    public void setDegrees(int degrees) {
        this.degrees = degrees;
    }
}
