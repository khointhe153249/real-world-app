package com.khoinguyen.realworldapp.controller;

import com.khoinguyen.realworldapp.exception.custom.CustomBadRequestException;
import com.khoinguyen.realworldapp.exception.custom.CustomNotFoundException;
import com.khoinguyen.realworldapp.model.user.dto.UserDTOCreate;
import com.khoinguyen.realworldapp.model.user.dto.UserDTOLoginRequest;
import com.khoinguyen.realworldapp.model.user.dto.UserDTOResponse;
import com.khoinguyen.realworldapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @PostMapping("/users/login")
    public Map<String, UserDTOResponse> login(@RequestBody Map<String, UserDTOLoginRequest> userLoginRequestMap)
            throws CustomBadRequestException {
        return userService.authenticate(userLoginRequestMap);
    }

    @PostMapping("/users")
    public Map<String, UserDTOResponse> registerUser(@RequestBody Map<String, UserDTOCreate> userDTOCreateMap) {
        return userService.registerUser(userDTOCreateMap);
    }

    @GetMapping("/user")
    public Map<String, UserDTOResponse> getCurrentUser() throws CustomNotFoundException {
        return userService.getCurrentUser();
    }
}
