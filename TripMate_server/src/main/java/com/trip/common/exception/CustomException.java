package com.trip.common.exception;

import org.springframework.http.HttpStatus;

import com.trip.common.Constants;

public class CustomException extends Exception {

    private static final long serialVersionUID = 4663380430591151694L;
    private Constants.ExceptionType exceptionType;
    private HttpStatus httpStatus;

    public CustomException(Constants.ExceptionType exceptionType, HttpStatus httpStatus,
                           String message) {
        super(exceptionType.toString() + message);
        this.exceptionType = exceptionType;
        this.httpStatus = httpStatus;
    }

    public Constants.ExceptionType getExceptionType() {
        return exceptionType;
    }

    public int getHttpStatusCode() {
        return httpStatus.value();
    }

    public String getHttpStatusType() {
        return httpStatus.getReasonPhrase();
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
