package com.green.greenearthforus.calendar.repository;

import com.green.greenearthforus.calendar.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
}
