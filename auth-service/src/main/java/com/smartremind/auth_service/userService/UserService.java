package com.smartremind.auth_service.userService;

import com.smartremind.auth_service.dto.RegistrationRequest;
import com.smartremind.auth_service.dto.UserResponse;


public interface UserService {

    public UserResponse saveUser(RegistrationRequest request);



}
