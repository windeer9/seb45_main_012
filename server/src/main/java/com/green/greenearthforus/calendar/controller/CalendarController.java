package com.green.greenearthforus.calendar.controller;

import com.green.greenearthforus.calendar.dto.CalendarDto;
import com.green.greenearthforus.calendar.service.CalendarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/calendar")
public class CalendarController {

    private final CalendarService calendarService;



    public CalendarController(CalendarService calendarService){
        this.calendarService = calendarService;

    }

    @PostMapping("/{user_id}")
    public ResponseEntity<CalendarDto.Response> postCalendar(@PathVariable("user_id") long userId){

        return new ResponseEntity<>( calendarService.createCalendar(userId), HttpStatus.CREATED);
    }

    @PatchMapping("/{user_id}/{calendar_id}")
    public ResponseEntity<CalendarDto.Response> patchCalendar(@PathVariable("user_id") long userId,
                                        @PathVariable("calendar_id") long calendarId,
                                        @Validated @RequestBody CalendarDto.Patch patch){

        return ResponseEntity.ok(calendarService.updateCalendar(patch, userId, calendarId));
    }

    @GetMapping("/{calendar_id}")
    public ResponseEntity<CalendarDto.Response> getCalendar(@PathVariable("calendar_id") long calendarId){

        return ResponseEntity.ok(calendarService.findCalendar(calendarId));
    }

    @DeleteMapping("/{calendar_id}")
    public ResponseEntity<Void> deleteCalendar(@PathVariable("calendar_id") long calendarId){
        calendarService.deleteCalendar(calendarId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAll(){
        calendarService.deleteAll();
        return ResponseEntity.noContent().build();
    }

}
