package com.green.greenEarthForUs.calendar.Controller;

import com.green.greenEarthForUs.calendar.Entity.Calendar;
import com.green.greenEarthForUs.calendar.DTO.CalendarDto;
import com.green.greenEarthForUs.calendar.Mapper.CalendarMapper;
import com.green.greenEarthForUs.calendar.Service.CalendarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/calendar/")
public class CalendarController {

    private final CalendarService calendarService;

    private final CalendarMapper mapper;

    public CalendarController(CalendarService calendarService,
                              CalendarMapper mapper){
        this.calendarService = calendarService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity postCalendar(@Validated @RequestBody CalendarDto.Post post){
        Calendar createdCalendar = calendarService.createCalendar(mapper.calendarPostDtoToCalendar(post));

        return new ResponseEntity(mapper.calendarToCalendarResponseDto(createdCalendar), HttpStatus.CREATED);
    }

    @PatchMapping("{user_id}/{calendar_id}")
    public ResponseEntity patchCalendar(@PathVariable("user_id") long userId,
                                        @PathVariable("calendar_id") long calendarId,
                                        @Validated @RequestBody CalendarDto.Patch patch){
        // user 검증 로직 필요
        patch.addCalendarId(calendarId);
        Calendar updatedCalendar = calendarService.updateCalendar(mapper.calendarPatchDtoToCalendar(patch));

        return ResponseEntity.ok(mapper.calendarToCalendarResponseDto(updatedCalendar));
    }

    @GetMapping("{calendar_id}")
    public ResponseEntity getCalendar(@PathVariable("calendar_id") long calendarId){
        Calendar findCalendar = calendarService.findCalendar(calendarId);

        return ResponseEntity.ok(mapper.calendarToCalendarResponseDto(findCalendar));
    }

    @DeleteMapping("{calendar_id}")
    public ResponseEntity deleteCalendar(@PathVariable("calendar_id") long calendarId){
        calendarService.deleteCalendar(calendarId);
        return ResponseEntity.noContent().build();
    }

}
