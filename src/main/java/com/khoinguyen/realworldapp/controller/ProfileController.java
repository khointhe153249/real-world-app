package com.khoinguyen.realworldapp.controller;

import com.khoinguyen.realworldapp.exception.custom.CustomNotFoundException;
import com.khoinguyen.realworldapp.model.profile.dto.ProfileDTOResponse;
import com.khoinguyen.realworldapp.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/profiles/{username}")
public class ProfileController {
    private final UserService userService;

    @GetMapping("")
    public Map<String, ProfileDTOResponse> getProfile(@PathVariable("username") String username)
            throws CustomNotFoundException {
        return userService.getProfile(username);
    }

    @PostMapping("/follow")
    public Map<String, ProfileDTOResponse> followUser(@PathVariable("username") String username)
            throws CustomNotFoundException {
        return userService.followUser(username);
    }

    @DeleteMapping("/unfollow")
    public Map<String, ProfileDTOResponse> unfollowUser(@PathVariable("username") String username)
            throws CustomNotFoundException {
        return userService.unfollowUser(username);
    }
}
