package com.green.greenEarthForUs.calendar.Mapper;

import com.green.greenEarthForUs.calendar.Entity.Calendar;
import com.green.greenEarthForUs.calendar.DTO.CalendarDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CalendarMapper {

    Calendar calendarPostDtoToCalendar(CalendarDto.Post requestBody);
    Calendar calendarPatchDtoToCalendar(CalendarDto.Patch requestBody);
    CalendarDto.Response calendarToCalendarResponseDto(Calendar calendar);

}
