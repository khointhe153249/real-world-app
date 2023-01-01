package com.khoinguyen.realworldapp.exception.custom;

import com.khoinguyen.realworldapp.model.CustomError;

public class CustomNotFoundException extends BaseCustomException{
    public CustomNotFoundException(CustomError customError) {
        super(customError);
    }
}
