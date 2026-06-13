package com.smartremind.user_service.service;



import com.fasterxml.classmate.types.ResolvedRecursiveType;
import com.smartremind.user_service.dto.ExpiryDateResponse;
import com.smartremind.user_service.dto.UserRequest;
import com.smartremind.user_service.dto.UserResponse;
import com.smartremind.user_service.entity.User;
import com.smartremind.user_service.enums.SubscriptionStatus;
import com.smartremind.user_service.exception.DuplicateException;
import com.smartremind.user_service.exception.InvalidPaginationException;
import com.smartremind.user_service.projection.UserExpiryProjection;
import com.smartremind.user_service.repository.UserRepository;
import jakarta.persistence.SecondaryTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.smartremind.common.exception.ResourceNotFoundException;


import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;


    private static  final int MAX_PAGE_SIZE = 100;

    private static final Set<String> ALLOWED_FIELD = Set.of(
            "id",
            "userName",
            "email",
            "createdAt",
            "updatedAt",
            "Status"


    );




    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }







    public UserResponse createUser(UserRequest userRequest){

        log.info("Request Create user | received");

        if (getUserByEmail(userRequest.email())!=null){

            throw  new DuplicateException("Email id already Exist");
        }

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


    public Page< UserResponse> getUserBySubscriptionStatus(SubscriptionStatus status , Pageable pageable) {
        log.info("Request get user by Status: {} | received", status);

        validatePageable(pageable);

        Page<User> users = userRepository.findByStatus(status, pageable);

        log.info("Request get user by Status: {} | Completed", status);


     return  users.map(this::userToResponseHelper);



    }

    //premium feature
//    public UserResponse enableSmsNotification(Long id) {
//
//    }

    //premium feature
    public ExpiryDateResponse getSubscriptionExpiry(Long id) {

//        User user = userRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found with  id: " + id, HttpStatus.NOT_FOUND));
//        return new ExpiryDateResponse(user.getSubscriptionExpiryDate());



UserExpiryProjection projection =
        userRepository.findProjectionById(id).orElseThrow(()-> new ResourceNotFoundException("User not found with  id: " + id,HttpStatus.NOT_FOUND));


        ExpiryDateResponse response = new ExpiryDateResponse(projection.getSubscriptionExpiryDate());

        return response;

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

        UserResponse response = new UserResponse( user.getId(), user.getUserName(), user.getEmail(), user.getFirstName(),
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


    private void validatePageable(Pageable pageable){
        if (pageable.getPageNumber()<0){
            throw  new InvalidPaginationException("Page number cannot be less than 0 ");
        }


        if (pageable.getPageSize()<=0 ||pageable.getPageSize()>100 ){

            throw  new InvalidPaginationException("Page size cannot be between 1 and "+MAX_PAGE_SIZE);
        }

        for (Sort.Order order : pageable.getSort()){
            String property = order.getProperty();

            if (!ALLOWED_FIELD.contains(property)){
                throw new InvalidPaginationException("Invalid sort field "+ property+" : not allowed ");
            }
        }



    }



}