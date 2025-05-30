package com.hackaton.desafio;


import com.hackaton.desafio.config.TokenService;
import com.hackaton.desafio.dto.authDTO.LoginRequest;
import com.hackaton.desafio.repository.UserRepository;
import com.hackaton.desafio.services.UserService;
import org.apache.juli.logging.Log;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private TokenService tokenService;



    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void return401WhenUserCredentialsAreNull(){
        LoginRequest user = new LoginRequest(null, null);

        ResponseEntity<?> response = userService.login(user);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void return401WhenUserNotFound(){
        LoginRequest user = new LoginRequest("new", "new");

        when(userRepository.findByName("new")).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.login(user);
        });

        assertEquals("User not found", exception.getMessage());
    }

}
