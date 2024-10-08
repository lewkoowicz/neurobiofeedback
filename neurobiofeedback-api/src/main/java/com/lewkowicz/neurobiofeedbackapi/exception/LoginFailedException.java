package com.lewkowicz.neurobiofeedbackapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class LoginFailedException extends RuntimeException {

    public LoginFailedException(String message) { super(message); }

}
