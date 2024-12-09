package com.trip.common.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.trip.member.ErrorResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> exceptionHandler(Exception e) {
        HttpHeaders responseHeaders = new HttpHeaders();
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        log.error("Advice 내 ExceptionHandler 호출, {}, {}", e.getCause(), e.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorType(httpStatus.getReasonPhrase())
                .msg("에러 발생")
                .build(), responseHeaders, httpStatus.value());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = CustomException.class)
    public ResponseEntity<ErrorResponse> customExceptionHandler(CustomException e) {
        HttpHeaders responseHeaders = new HttpHeaders();
        log.error("Advice 내 customExceptionHandler 호출, {}, {}", e.getCause(), e.getMessage());
        return new ResponseEntity<>(ErrorResponse.builder()
                .errorType(e.getHttpStatusType())
                .msg(e.getMessage())
                .build(), responseHeaders, e.getHttpStatusCode());
    }

}
