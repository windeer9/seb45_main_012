package com.green.greenEarthForUs.calendar.Repository;

import com.green.greenEarthForUs.calendar.Entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalendarRepository extends JpaRepository<Calendar, Long> {
}
