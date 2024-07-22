package com.lewkowicz.neurobiofeedbackapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidBookingDateException extends RuntimeException {

    public InvalidBookingDateException(String message) { super(message); }

}
