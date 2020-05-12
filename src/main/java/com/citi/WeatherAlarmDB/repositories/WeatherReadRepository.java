package com.citi.WeatherAlarmDB.repositories;

import com.citi.WeatherAlarmDB.Models.Customer;
import com.citi.WeatherAlarmDB.Models.WeatherRead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface WeatherReadRepository extends JpaRepository<WeatherRead, Long> {
    @Query(name = "WeatherAlarm.customer")
    List<WeatherRead> findWeatherReadByCustomer(@Param("customer") Customer customer);

   /* @Query(name = "WeatherAlarm.customer" "WeatherAlarm.time")
    WeatherRead findWeatherReadByCustomerAndDate(@Param("customer") Customer customer, @Param("time") Date time);*/
}
