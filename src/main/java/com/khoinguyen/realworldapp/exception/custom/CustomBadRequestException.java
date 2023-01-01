package com.khoinguyen.realworldapp.exception.custom;

import com.khoinguyen.realworldapp.model.CustomError;

public class CustomBadRequestException extends BaseCustomException{
    public CustomBadRequestException(CustomError customError) {
        super(customError);
    }
}
