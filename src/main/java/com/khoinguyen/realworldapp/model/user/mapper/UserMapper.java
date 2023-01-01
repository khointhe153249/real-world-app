package com.khoinguyen.realworldapp.model.user.mapper;

import com.khoinguyen.realworldapp.entity.User;
import com.khoinguyen.realworldapp.model.user.dto.UserDTOCreate;
import com.khoinguyen.realworldapp.model.user.dto.UserDTOResponse;

public class UserMapper {
    public static UserDTOResponse toUserDTOResponse(User user) {
        return UserDTOResponse.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .bio(user.getBio())
                .image(user.getImage())
                .build();
    }

    public static User toUser(UserDTOCreate userDTOCreate) {
        return User.builder()
                .username(userDTOCreate.getUsername())
                .password(userDTOCreate.getPassword())
                .email(userDTOCreate.getEmail())
                .build();
    }
}
