package com.green.greenEarthForUs.calendar.Service;

import com.green.greenEarthForUs.Exception.BusinessLogicException;
import com.green.greenEarthForUs.Exception.ExceptionCode;
import com.green.greenEarthForUs.calendar.Repository.CalendarRepository;
import com.green.greenEarthForUs.calendar.Entity.Calendar;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CalendarService {

    private final CalendarRepository calendarRepository;

    public CalendarService(CalendarRepository calendarRepository){
        this.calendarRepository = calendarRepository;
    }

    public Calendar createCalendar(Calendar calendar){

        return calendarRepository.save(calendar);
    }

    public Calendar updateCalendar(Calendar calendar){
        Calendar findCalendar = findVerifiedCalendar(calendar.getCalendarId());

        Optional.ofNullable(calendar.getBody())
                .ifPresent(body -> findCalendar.setBody(body));

        return calendarRepository.save(findCalendar);
    }

    public Calendar findCalendar(long calendarId){
        return calendarRepository.findById(calendarId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CALENDAR_NOT_FOUND));
    }

    public void deleteCalendar(long calendarId){
        Calendar calendar = findCalendar(calendarId);
        calendarRepository.delete(calendar);
    }

    public Calendar findVerifiedCalendar(long calendarId){
        Optional<Calendar> optionalCalendar =
                calendarRepository.findById(calendarId);
        Calendar findCalendar =
                optionalCalendar.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.CALENDAR_NOT_FOUND));
        return findCalendar;
    }


}
