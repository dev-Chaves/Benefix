package com.hackaton.desafio.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.hackaton.desafio.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    private final long expirationTime = 86400000;

    private String generateToken(UserEntity user){
        try {

            if(user == null || user.getName().isEmpty()){
                throw new IllegalArgumentException("User cannot be null or empty");
            }

            Algorithm algorithm = Algorithm.HMAC256(secret);

            Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

            String token = JWT.create()
                    .withSubject(user.getName())
                    .withIssuer("API Benefity")
                    .withExpiresAt(expirationDate)
                    .sign(algorithm);

            return token;

        } catch (JWTCreationException e){
            throw new RuntimeException("Error generating token: " + e.getMessage());
        }
    }

    public String isTokenValid(String token){
        try {
            if(token == null || token.isEmpty()){
                return null;
            }

            Algorithm algorithm = Algorithm.HMAC256(secret);
            String subject = JWT.require(algorithm)
                    .withIssuer("API Benefity")
                    .build()
                    .verify(token)
                    .getSubject();

            return subject;

        } catch (JWTVerificationException e){
            throw new RuntimeException("Error validating token: " + e.getMessage());
        }
    }

    public Date getDateExpiration(String token){
        try {
            if(token == null || token.isEmpty()){
                return null;
            }

            Algorithm algorithm = Algorithm.HMAC256(secret);
            Date expirationDate = JWT.require(algorithm)
                    .withIssuer("API Benefity")
                    .build()
                    .verify(token)
                    .getExpiresAt();

            return expirationDate;

        } catch (Exception e){
            throw new RuntimeException("Error getting expiration date: " + e.getMessage());
        }
    }

}
