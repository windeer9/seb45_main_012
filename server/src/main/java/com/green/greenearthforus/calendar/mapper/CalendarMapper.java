package com.green.greenearthforus.calendar.mapper;

import com.green.greenearthforus.calendar.entity.Calendar;
import com.green.greenearthforus.calendar.dto.CalendarDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CalendarMapper {

    @Mapping(source = "user.userId", target = "userId")
    CalendarDto.Response calendarToCalendarResponseDto(Calendar calendar);
    Calendar calendarResponseDtoToCalendar(CalendarDto.Response response);
}
