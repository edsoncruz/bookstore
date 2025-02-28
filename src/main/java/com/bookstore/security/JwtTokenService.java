package com.bookstore.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.bookstore.entity.User;
import com.bookstore.exception.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class JwtTokenService {

    private static final String ISSUER = "bookstore-api";

    private static final Boolean EXPIRES = false;

    @Value("{bookstore.jwt.secretKey:lKJASlçjÇLKjflçkjflskdjflçkdsjfçlsdjflslksdnjf}")
    private String secretKey;

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);

            JWTCreator.Builder tokenBuilder =  JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(user.getUsername());

            if(EXPIRES){
                tokenBuilder = tokenBuilder
                        .withIssuedAt(creationDate())
                        .withExpiresAt(expirationDate());
            }

            return tokenBuilder.sign(algorithm);

        } catch (JWTCreationException e){
            throw new BadRequestException("It was not possible to generate the token", e);
        }
    }

    public String getSubjectFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secretKey);
            return JWT.require(algorithm)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException e){
            throw new BadRequestException("Token is invalid");
        }
    }

    private Instant creationDate() {
        return Instant.now();
    }

    private Instant expirationDate() {
        return Instant.now().plus(180, ChronoUnit.DAYS);
    }

}
