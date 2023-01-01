package com.khoinguyen.realworldapp.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenPayLoad {
    private int userId;
    private String email;
}
