package com.citi.WeatherAlarmDB.Models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table
public class Customer {
    @Id
    @Column
    private String uuid;

    @Column
    private boolean isPremium;

    @Column
    private int minuteBeforeNextRead = 15;

    @OneToMany
    private List<WeatherRead> read;

    public Customer() {
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean getIsPremium() {
        return isPremium;
    }

    public void setIsPremium(boolean premium) {
        isPremium = premium;
    }

    public int getMinuteBeforeNextRead() {
        return minuteBeforeNextRead;
    }

    public void setMinuteBeforeNextRead(int minuteBeforeNextRead) {
        this.minuteBeforeNextRead = minuteBeforeNextRead;
    }

    public List<WeatherRead> getRead() {
        return read;
    }

    public void setRead(List<WeatherRead> read) {
        this.read = read;
    }
}
