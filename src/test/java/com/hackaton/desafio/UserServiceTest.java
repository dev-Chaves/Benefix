package com.hackaton.desafio;


import com.hackaton.desafio.config.TokenService;
import com.hackaton.desafio.dto.authDTO.LoginRequest;
import com.hackaton.desafio.dto.authDTO.RegisterDTO;
import com.hackaton.desafio.dto.userDTO.UserRequest;
import com.hackaton.desafio.entity.EnterpriseEntity;
import com.hackaton.desafio.repository.EnterpriseRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EnterpriseRepository enterpriseRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ValidationUtil validationUtil;

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

    @Test
    void registerTest(){
        RegisterDTO user = new RegisterDTO(
                "new",
                "new",
                1L,
                "token"
        );

        when(userRepository.findByName("new")).thenReturn(Optional.empty());
        EnterpriseEntity enterprise = new EnterpriseEntity();
        when(enterpriseRepository.findById(1L)).thenReturn(Optional.of(enterprise));
        when(passwordEncoder.encode("new")).thenReturn("encodedPassword");

        ResponseEntity<?> response = userService.register(user);

        assertEquals(201, response.getStatusCode().value());
    }

}
