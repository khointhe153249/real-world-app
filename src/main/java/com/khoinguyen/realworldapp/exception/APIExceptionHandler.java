package com.khoinguyen.realworldapp.exception;

import com.khoinguyen.realworldapp.exception.custom.CustomBadRequestException;
import com.khoinguyen.realworldapp.exception.custom.CustomNotFoundException;
import com.khoinguyen.realworldapp.model.CustomError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class APIExceptionHandler {
    @ExceptionHandler(CustomBadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, CustomError> badRequestException(CustomBadRequestException ex) {
        return ex.getErrors();
    }

    @ExceptionHandler(CustomNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, CustomError> customNotFoundException(CustomNotFoundException ex) {
        return ex.getErrors();
    }
}
