package com.green.greenEarthForUs.calendar.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

public class CalendarDto {
    @Getter
    @AllArgsConstructor
    public static class Post{
        private String body;
        private long userId;
        private long postId;
        private LocalDateTime postCreatedAt;
    }
    @Getter
    public static class Patch{
        private String body;
        private long calendarId;

        public void addCalendarId(long calendarId){this.calendarId = calendarId;}
    }
    @Getter
    @Builder
    public static class Response{
        private long id;
        private String body;
        private long userId;
        private long postId;
        private LocalDateTime postCreatedAt;
    }


}
