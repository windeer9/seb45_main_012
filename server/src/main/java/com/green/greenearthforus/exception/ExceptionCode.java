package com.green.greenearthforus.exception;

import lombok.Getter;

public enum ExceptionCode {
    VOTE_EXISTS(409, "Vote exists"),
    IMAGE_NOT_FOUND(404, "Image Not Found"),
    FILE_UPLOAD_FAIL(404, "File Upload Fail"),
    IMAGE_NOT_SUPPORT(404, "Image Not Support"),
    USER_NOT_FOUND(404, "User not found"),
    USER_EXISTS(409, "User exists"),
    CALENDAR_NOT_FOUND(404, "Calendar not found"),
    CALENDAR_EXISTS(409, "Calendar exists"),
    VOTE_NOT_FOUND(404, "Vote not found"),
    NOT_IMPLEMENTATION(501, "Not Implementation"),
    INVALID_USER_STATUS(400, "Invalid member status"),
    COMMENT_NOT_FOUND(404,"comment not found"),

    INVALID_COMMENT_ACCESS(1001,"You do not have permission to access comments");

    @Getter
    private final int status;

    @Getter
    private final String message;

    ExceptionCode(int code, String message) {
        this.status = code;
        this.message = message;
    }
}
