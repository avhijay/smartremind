package com.smartremind.user_service.service;



import com.smartremind.user_service.dto.ExpiryDateResponse;
import com.smartremind.user_service.dto.UserRequest;
import com.smartremind.user_service.dto.UserResponse;
import com.smartremind.user_service.entity.User;
import com.smartremind.user_service.enums.SubscriptionStatus;
import com.smartremind.user_service.projection.UserExpiryProjection;
import com.smartremind.user_service.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.smartremind.common.exception.ResourceNotFoundException;

import java.time.Instant;
import java.util.List;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse createUser(UserRequest userRequest){

        log.info("Request Create user | received");

        User user = requestToUserHelper(userRequest);

        userRepository.save(user);
        log.info("Request Create user | completed: {} ", user.getId());

        return userToResponseHelper(user);


    }

    public UserResponse getUserById(Long id) {
        log.info("Request get user by Id: {} | received", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with  id: " + id, HttpStatus.NOT_FOUND));
        log.info("Request get user by Id: {} | completed", id);


       return userToResponseHelper(user);




    }


    public List< UserResponse> getUserBySubscriptionStatus(SubscriptionStatus status) {
        log.info("Request get user by Status: {} | received", status);

        List<User> users = userRepository.findByStatus(status);

        log.info("Request get user by Status: {} | Completed", status);

        return users.stream().map(this::userToResponseHelper).toList();



    }

    //premium feature
//    public UserResponse enableSmsNotification(Long id) {
//
//    }

    //premium feature
    public ExpiryDateResponse getSubscriptionExpiry(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with  id: " + id, HttpStatus.NOT_FOUND));
        return new ExpiryDateResponse(user.getSubscriptionExpiryDate());


    }


    // implement after payment service
    public void activateUserSubscription() {


    }

    public UserResponse getUserByName(String userName) {

        log.info("Request get user by userName: {} | received", userName);
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with  userName " + userName, HttpStatus.NOT_FOUND));
        log.info("Request get user by userName: {} | completed", userName);


        return userToResponseHelper(user);


    }

    public UserResponse getUserByEmail(String emailId) {

        log.info("Request get user by Email {} | received", emailId);
        User user = userRepository.findByEmail(emailId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with Email " + emailId, HttpStatus.NOT_FOUND));
        log.info("Request get user by Email: {} | completed", emailId);


        return userToResponseHelper(user);


    }


    private UserResponse userToResponseHelper(User user){

        UserResponse response = new UserResponse(user.getUserName(), user.getEmail(), user.getFirstName(),
                user.getLastName(),user.getStatus(),user.getSubscriptionExpiryDate(),user.getTimeZone(),
                user.getLanguages(),user.isEmailNotificationEnabled(),user.isSmsNotificationEnabled(),
                user.isPushNotificationEnabled(),user.getCreatedAt(),user.getUpdatedAt());

        return response;

    }


    private User requestToUserHelper(UserRequest request){
        User user = new User();

        user.setUserName(request.userName());
        user.setEmail(request.email());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setTimeZone(request.timeZone());
        user.setLanguages(request.languages());
        user.setEmailNotificationEnabled(request.emailNotificationEnabled());
        user.setSmsNotificationEnabled(request.smsNotificationEnabled());
        user.setPushNotificationEnabled(request.pushNotificationEnabled());
        return user;
    }



}