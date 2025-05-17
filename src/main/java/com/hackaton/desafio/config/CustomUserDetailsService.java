package com.hackaton.desafio.config;

import com.hackaton.desafio.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if(username == null || username.isEmpty()){
            throw new UsernameNotFoundException("Username cannot be null or empty");
        }
        var user = userRepository.findByName(username);
        if(user == null){
            throw new UsernameNotFoundException("User not found");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getName(),
                user.getPassword(),
                new ArrayList<>()
        );
    }
}
