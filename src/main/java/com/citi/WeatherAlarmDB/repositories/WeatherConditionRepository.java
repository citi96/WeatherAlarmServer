package com.citi.WeatherAlarmDB.repositories;

import com.citi.WeatherAlarmDB.Models.WeatherCondition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherConditionRepository extends JpaRepository<WeatherCondition, String> {
}
