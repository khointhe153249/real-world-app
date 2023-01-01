package com.khoinguyen.realworldapp.service;

import com.khoinguyen.realworldapp.exception.custom.CustomBadRequestException;
import com.khoinguyen.realworldapp.exception.custom.CustomNotFoundException;
import com.khoinguyen.realworldapp.model.profile.dto.ProfileDTOResponse;
import com.khoinguyen.realworldapp.model.user.dto.UserDTOCreate;
import com.khoinguyen.realworldapp.model.user.dto.UserDTOLoginRequest;
import com.khoinguyen.realworldapp.model.user.dto.UserDTOResponse;

import java.util.Map;

public interface UserService {
    public Map<String, UserDTOResponse>  authenticate(Map<String, UserDTOLoginRequest> userLoginRequestMap)
            throws CustomBadRequestException;

    public Map<String, UserDTOResponse> registerUser(Map<String, UserDTOCreate> userDTOCreateMap);

    public Map<String, UserDTOResponse> getCurrentUser() throws CustomNotFoundException;

    public Map<String, ProfileDTOResponse> getProfile(String username) throws CustomNotFoundException;

    public Map<String, ProfileDTOResponse> followUser(String username) throws CustomNotFoundException;

    public Map<String, ProfileDTOResponse> unfollowUser(String username) throws CustomNotFoundException;
}
