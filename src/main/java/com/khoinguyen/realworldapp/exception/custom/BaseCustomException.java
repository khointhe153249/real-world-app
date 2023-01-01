package com.khoinguyen.realworldapp.exception.custom;

import com.khoinguyen.realworldapp.model.CustomError;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class BaseCustomException extends Exception{
    private Map<String, CustomError> errors;
    public BaseCustomException(CustomError customError) {
        this.errors = new HashMap<>();
        this.errors.put("errors", customError);
    }
}
