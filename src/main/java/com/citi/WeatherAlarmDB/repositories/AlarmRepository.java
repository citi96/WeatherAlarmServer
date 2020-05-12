package com.citi.WeatherAlarmDB.repositories;

import com.citi.WeatherAlarmDB.Models.Alarm;
import com.citi.WeatherAlarmDB.Models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    @Query(name = "Alarm.customer")
    List<Alarm> findAlarmsByCustomer(@Param("customer") Customer customer);
}
