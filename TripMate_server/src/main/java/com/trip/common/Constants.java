package com.trip.common;

public class Constants {

    public enum ExceptionType {

        AUTHENTICATION("Authentication"),
        INVALID_TOKEN("Invalid Token"), // 새로운 예외 타입 추가
        AUTHORIZATION("Authorization"); // 다른 예외 타입 예시

        private String exceptionType;

        ExceptionType(String exceptionType) {
            this.exceptionType = exceptionType;
        }

        public String getExceptionType() {
            return exceptionType;
        }

        @Override
        public String toString() {
            return getExceptionType() + " Exception. ";
        }

    }

}
