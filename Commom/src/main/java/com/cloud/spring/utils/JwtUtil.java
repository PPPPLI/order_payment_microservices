package com.cloud.spring.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * Token generator and verifier tool
 * */
@Component
public class JwtUtil {

    private static final String SIGNATURE;
    private static final JWTVerifier VERIFIER;

    static{
        SIGNATURE = "bWljcm9zZXJ2aWNlcw";
        VERIFIER = JWT.require(Algorithm.HMAC256(SIGNATURE)).build();
    }

    public static String createJwtToken(String user){

        Map<String,Object> map = Map.of("alg","HMAC256","typ","JWT");

        return JWT.create().withHeader(map)
                .withExpiresAt(Instant.now().plus(60,ChronoUnit.MINUTES))
                .withIssuer("LP")
                .withClaim("user",user)
                .sign(Algorithm.HMAC256(SIGNATURE));
    }

    public static boolean verifyToken(String token){

        try {
            VERIFIER.verify(token);
            return true;
        }catch (JWTVerificationException e){
            return false;
        }
    }

    public static String getUserInfoFromJWT(String token){

        DecodedJWT decodedJWT = VERIFIER.verify(token);
        return decodedJWT.getClaim("user").asString();
    }
}
