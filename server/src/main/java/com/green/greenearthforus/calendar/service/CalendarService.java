package com.green.greenearthforus.calendar.service;

import com.green.greenearthforus.exception.BusinessLogicException;
import com.green.greenearthforus.exception.ExceptionCode;
import com.green.greenearthforus.calendar.dto.CalendarDto;
import com.green.greenearthforus.calendar.mapper.CalendarMapper;
import com.green.greenearthforus.calendar.repository.CalendarRepository;
import com.green.greenearthforus.calendar.entity.Calendar;
import com.green.greenearthforus.user.entity.User;
import com.green.greenearthforus.user.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CalendarService {

    private final CalendarRepository calendarRepository;
    private final CalendarMapper mapper;
    private final UserService userService;

    public CalendarService(CalendarRepository calendarRepository,
                           CalendarMapper mapper,
                           UserService userService
                           ) {
        this.calendarRepository = calendarRepository;
        this.mapper = mapper;
        this.userService = userService;
    }

    public CalendarDto.Response createCalendar(long userId) {
        User user = userService.getUser(userId);
        if(user.getCalendar() !=null){
            throw new BusinessLogicException(ExceptionCode.CALENDAR_EXISTS);
        }

        Calendar createdCalendar = new Calendar();
        createdCalendar.setUser(user);
        List<LocalDate> date = new ArrayList<>();
        createdCalendar.setStampedDates(date);

        return mapper.calendarToCalendarResponseDto(calendarRepository.save(createdCalendar));
    }

    public CalendarDto.Response updateCalendar(CalendarDto.Patch calendar, long userId, long calendarId) {
        userService.getUser(userId);
        Calendar findCalendar = findVerifiedCalendar(calendarId);

        Optional.ofNullable(calendar.getBody())
                .ifPresent(findCalendar::setBody);
        Optional.ofNullable(calendar.getStampedDates())
                .ifPresent(findCalendar::setStampedDates);
        return mapper.calendarToCalendarResponseDto(calendarRepository.save(findCalendar));
    }

    public CalendarDto.Response findCalendar(long calendarId) {

        return mapper.calendarToCalendarResponseDto(findVerifiedCalendar(calendarId));
    }

    public void deleteCalendar(long calendarId) {
        calendarRepository.delete(findVerifiedCalendar(calendarId));
    }

    public void deleteAll(){
        calendarRepository.deleteAll();
    }

    public Calendar findVerifiedCalendar(long calendarId) {
        Optional<Calendar> optionalCalendar =
                calendarRepository.findById(calendarId);
        return optionalCalendar.orElseThrow(() ->
                        new BusinessLogicException(ExceptionCode.CALENDAR_NOT_FOUND));
    }

    public void updateStampedDate(long userId) {

        User user = userService.getUser(userId);
        Calendar find;
        if (user.getCalendar() != null) {
            find = findVerifiedCalendar(user.getCalendar().getCalendarId());
        } else {
            find = mapper.calendarResponseDtoToCalendar(createCalendar(userId));
        }
        if (find != null) {
            List<LocalDate> stampedDate = find.getStampedDates();
            if (stampedDate == null) {
                stampedDate = new ArrayList<>();
                find.setStampedDates(stampedDate);
            }
            stampedDate.add(LocalDate.now());
        }
    }


}
