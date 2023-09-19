package com.green.greenearthforus.calendar.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

public class CalendarDto {
   private CalendarDto(){
       throw new IllegalStateException("calednar dto");
   }
    @Getter
    @Setter
    public static class Patch{
        private String body;
        private long calendarId;
        private List<LocalDate> stampedDates;
    }
    @Getter
    @Setter
    @Builder
    public static class Response{
        private long calendarId;
        private String body;
        private long userId;
        private long postId;
        private List<LocalDate> stampedDates;
    }
}
