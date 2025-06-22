package com.hackaton.desafio.services;

import com.hackaton.desafio.config.TokenService;
import com.hackaton.desafio.dto.IA.DoubtRequest;
import com.hackaton.desafio.dto.IA.DoubtResponse;
import com.hackaton.desafio.dto.authDTO.LoginRequestV2;
import com.hackaton.desafio.dto.authDTO.LoginResponse;
import com.hackaton.desafio.dto.authDTO.RegisterDTO;
import com.hackaton.desafio.dto.authDTO.RegisterResponse;
import com.hackaton.desafio.entity.EnterpriseEntity;
import com.hackaton.desafio.entity.IA.DoubtEntity;
import com.hackaton.desafio.entity.Role.Role;
import com.hackaton.desafio.entity.UserEntity;
import com.hackaton.desafio.repository.DoubtRepository;
import com.hackaton.desafio.repository.EnterpriseRepository;
import com.hackaton.desafio.repository.UserRepository;
import com.hackaton.desafio.util.AuthUtil;
import com.hackaton.desafio.util.EncryptionUtil;
import com.hackaton.desafio.util.validation.validators.LoginValidatorCpf;
import com.hackaton.desafio.util.validation.validators.RegisterValidatorCPF;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final EnterpriseRepository enterpriseRepository;
    private final DoubtRepository doubtRepository;
    private final EncryptionUtil encryptionUtil;
    private final LoginValidatorCpf loginValidatorCpf;
    private final RegisterValidatorCPF registerValidatorCPF;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService, EnterpriseRepository enterpriseRepository, DoubtRepository doubtRepository, EncryptionUtil encryptionUtil, LoginValidatorCpf loginValidatorCpf, RegisterValidatorCPF registerValidatorCPF) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;

        this.enterpriseRepository = enterpriseRepository;
        this.doubtRepository = doubtRepository;
        this.encryptionUtil = encryptionUtil;
        this.loginValidatorCpf = loginValidatorCpf;
        this.registerValidatorCPF = registerValidatorCPF;
    }

    public ResponseEntity<?> login(LoginRequestV2 userRequest) {

        loginValidatorCpf.validate(userRequest);

        try {

            System.out.println(userRequest.cpf());

            String encryptedCPF = encryptionUtil.encrypt(userRequest.cpf());

            System.out.println(encryptedCPF);

            UserEntity user = userRepository.findByCpf(encryptedCPF).orElseThrow(() -> new UsernameNotFoundException("User not found"));

            String token;

            if (passwordEncoder.matches(userRequest.password(), user.getPassword())) {
                token = this.tokenService.generateToken(user);
            } else if (user.getPassword().equals(userRequest.password()) ) {
                token = this.tokenService.generateToken(user);
            } else {
                return ResponseEntity.status(401).body("Invalid credentials");
            }

            LoginResponse response = new LoginResponse(user.getName(), token);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Login error: " + e.getMessage());
        }

    }

    public ResponseEntity<?> register (RegisterDTO userRequest) {

        try{
            String sb = "token";

//        registerValidator.validate(userRequest);

            registerValidatorCPF.validate(userRequest);

            if(!userRequest.token().contentEquals(sb)){
                return ResponseEntity.status(401).body("Invalid token");
            }

            EnterpriseEntity enterprise = enterpriseRepository.findById(userRequest.enterprise()).orElseThrow(()-> new RuntimeException("Enterprise not found"));

            String encryptedCPF = encryptionUtil.encrypt(userRequest.cpf());

            UserEntity newUser = new UserEntity();
            newUser.setName(userRequest.name());
            newUser.setCpf(encryptedCPF);
            newUser.setPassword(passwordEncoder.encode(userRequest.password()));
            newUser.setEnterprise(enterprise);
            newUser.setCreatedAt(LocalDateTime.now());
            newUser.setRole(Role.ADMIN);

            userRepository.save(newUser);

            RegisterResponse response = new RegisterResponse(newUser.getName(), newUser.getRole());

            return ResponseEntity.status(201).body(response);
        }catch (Exception e){
            throw new RuntimeException("User failed to register");
        }


    }

    public ResponseEntity<DoubtResponse> createDoubt(DoubtRequest doubtRequest){

        UserEntity user = AuthUtil.getAuthenticatedUser().orElseThrow(()-> new RuntimeException("User not authenticated"));

        DoubtEntity doubt = new DoubtEntity();

        doubt.setQuestion(doubtRequest.question());
        doubt.setAnswered(false);
        doubt.setCreatedAt(LocalDateTime.now());
        doubt.setUser(user);

        DoubtResponse response = new DoubtResponse(doubt.getQuestion(), doubt.getUser().getName(), doubt.getAnswered());

        doubtRepository.save(doubt);

        return ResponseEntity.ok().body(response);

    }


}
