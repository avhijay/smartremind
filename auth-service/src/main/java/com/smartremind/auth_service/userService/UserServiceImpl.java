package com.smartremind.auth_service.userService;

import com.smartremind.common.exception.ValidationException;

import com.smartremind.auth_service.dto.RegistrationRequest;
import com.smartremind.auth_service.dto.UserResponse;
import com.smartremind.auth_service.entity.User;
import com.smartremind.auth_service.enums.Role;
import com.smartremind.auth_service.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service

public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository  , PasswordEncoder passwordEncoder ){
        this.userRepository  = userRepository;
        this.passwordEncoder=passwordEncoder;
    }


    @Override // no helper method due to single method
    public UserResponse saveUser(RegistrationRequest request) {
        log.info("Request (User Registration received ) : incomplete ");

        if (userRepository.findByUsername(request.username()).isPresent()){
            throw  new ValidationException("Username  already exist | please try logging in with existing credentials ");


        }


        User user = User.builder()
                .role(Role.ROLE_USER)
                .username(request.username())
                .password(passwordEncoder.encode(  request.password()))
                .enabled(true).build();
        userRepository.save(user);
        log.info("User registration {} : completed",user.getId());

        return new UserResponse(user.getId(), user.getUsername(), user.getRole(),user.getEnabled());


    }
}
