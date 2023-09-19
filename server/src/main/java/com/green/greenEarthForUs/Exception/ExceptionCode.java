package com.green.greenEarthForUs.Exception;

import lombok.Getter;

public enum ExceptionCode {
    USER_NOT_FOUND(404, "User not found"),
    USER_EXISTS(409, "User exists"),
    CALENDAR_NOT_FOUND(404, "Calendar not found"),
    CALENDAR_EXISTS(409, "Calendar exists"),
    VOTE_NOT_FOUND(404, "Vote not found"),
    NOT_IMPLEMENTATION(501, "Not Implementation"),
    INVALID_USER_STATUS(400, "Invalid member status");

    @Getter
    private int status;

    @Getter
    private String message;

    ExceptionCode(int code, String message) {
        this.status = code;
        this.message = message;
    }
}
