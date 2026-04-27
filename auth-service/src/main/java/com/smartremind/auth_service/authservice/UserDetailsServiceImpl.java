package com.smartremind.auth_service.authservice;


import com.smartremind.auth_service.entity.User;
import com.smartremind.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(()->
                new UsernameNotFoundException
                        ("Username not found : "+username));

// already have User entity so needed to import the full package -
        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername()).
                password(user.getPassword()).authorities(user.getRole().name()).build();
    }
}
