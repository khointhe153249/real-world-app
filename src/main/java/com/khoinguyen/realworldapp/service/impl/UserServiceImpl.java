package com.khoinguyen.realworldapp.service.impl;

import com.khoinguyen.realworldapp.entity.User;
import com.khoinguyen.realworldapp.exception.custom.CustomBadRequestException;
import com.khoinguyen.realworldapp.exception.custom.CustomNotFoundException;
import com.khoinguyen.realworldapp.model.CustomError;
import com.khoinguyen.realworldapp.model.profile.dto.ProfileDTOResponse;
import com.khoinguyen.realworldapp.model.user.dto.UserDTOCreate;
import com.khoinguyen.realworldapp.model.user.dto.UserDTOLoginRequest;
import com.khoinguyen.realworldapp.model.user.dto.UserDTOResponse;
import com.khoinguyen.realworldapp.model.user.mapper.UserMapper;
import com.khoinguyen.realworldapp.repository.UserRepository;
import com.khoinguyen.realworldapp.service.UserService;
import com.khoinguyen.realworldapp.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Map<String, UserDTOResponse> authenticate(Map<String, UserDTOLoginRequest> userLoginRequestMap) throws CustomBadRequestException {
        UserDTOLoginRequest userDTOLoginRequest = userLoginRequestMap.get("user");
        Optional<User> userOptional = userRepository.findByEmail(userDTOLoginRequest.getEmail());

        boolean isSuccess = false;
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(userDTOLoginRequest.getPassword(), user.getPassword())) {
                isSuccess = true;
            }
        }
        if (!isSuccess) {
            throw new CustomBadRequestException(CustomError.builder()
                    .code("400")
                    .message("Username name and password is correct")
                    .build());
        }
        return buildDTOResponse(userOptional.get());
    }

    @Override
    public Map<String, UserDTOResponse> registerUser(Map<String, UserDTOCreate> userDTOCreateMap) {
        UserDTOCreate userDTOCreate = userDTOCreateMap.get("user");
        User user = UserMapper.toUser(userDTOCreate);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);
        return buildDTOResponse(user);
    }

    @Override
    public Map<String, UserDTOResponse> getCurrentUser() throws CustomNotFoundException {
        User userLoggedIn = getUserLoggedIn();
        if(userLoggedIn!=null) {
            return buildDTOResponse(userLoggedIn);
        }
        throw new CustomNotFoundException(CustomError.builder()
                .code("404")
                .message("User not exist")
                .build());
    }

    public User getUserLoggedIn() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            User user = userRepository.findByEmail(email).get();
            return user;
        }
        return null;
    }

    @Override
    public Map<String, ProfileDTOResponse> getProfile(String username) throws CustomNotFoundException {
        User userLoggedIn = getUserLoggedIn();

        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isEmpty()) {
            throw new CustomNotFoundException(CustomError.builder()
                    .code("404")
                    .message("User Not Found")
                    .build());
        }
        User user = userOptional.get();
        Set<User> followers = user.getFollowers(); 
        
        boolean isFollowing = false;
        for (User u : followers) {
            if(u.getId() == userLoggedIn.getId()) {
                isFollowing = true;
                break; 
            }
        }
        
        return buildProfileResponse(userOptional.get(), isFollowing);
    }

    private Map<String, ProfileDTOResponse> buildProfileResponse(User user, boolean isFollowing) {
        Map<String, ProfileDTOResponse> wrapper = new HashMap<>();
        ProfileDTOResponse profileDTOResponse = ProfileDTOResponse.builder()
                .username(user.getUsername())
                .bio(user.getBio())
                .image(user.getImage())
                .following(isFollowing)
                .build();
        wrapper.put("profile", profileDTOResponse);
        return wrapper;
    }

    private Map<String, UserDTOResponse> buildDTOResponse(User user) {
        Map<String, UserDTOResponse> wrapper = new HashMap<>();
        UserDTOResponse userDTOResponse = UserMapper.toUserDTOResponse(user);
        userDTOResponse.setToken(jwtTokenUtil.generateToken(user, 24 * 60 * 60));
        wrapper.put("user", userDTOResponse);
        return wrapper;
    }

    @Override
    public Map<String, ProfileDTOResponse> followUser(String username) throws CustomNotFoundException {
        User userLoggedIn = getUserLoggedIn();

        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isEmpty()) {
            throw new CustomNotFoundException(CustomError.builder()
                    .code("404")
                    .message("User Not Found")
                    .build());
        }
        User user = userOptional.get();
        Set<User> followers = user.getFollowers(); 
        
        boolean isFollowing = false;
        for (User u : followers) {
            if(u.getId() == userLoggedIn.getId()) {
                isFollowing = true;
                break; 
            }
        }
        if(!isFollowing) {
            isFollowing = true;
            user.getFollowers().add(userLoggedIn);
            userRepository.save(user);
            isFollowing = true;
        }
        return buildProfileResponse(user, isFollowing);
    }

    @Override
    public Map<String, ProfileDTOResponse> unfollowUser(String username) throws CustomNotFoundException {
        User userLoggedIn = getUserLoggedIn();

        Optional<User> userOptional = userRepository.findByUsername(username);
        if(userOptional.isEmpty()) {
            throw new CustomNotFoundException(CustomError.builder()
                    .code("404")
                    .message("User Not Found")
                    .build());
        }
        User user = userOptional.get();
        Set<User> followers = user.getFollowers(); 
        
        boolean isFollowing = false;
        for (User u : followers) {
            if(u.getId() == userLoggedIn.getId()) {
                isFollowing = true;
                break; 
            }
        }
        if(isFollowing) {
            isFollowing = true;
            user.getFollowers().remove(userLoggedIn);
            userRepository.save(user);
            isFollowing = false;
        }
        return buildProfileResponse(user, isFollowing); 
    }
}
