package com.bookai.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;


@Component
public class JwtUtil {
    private static final String SecretKey= "mySecretKey123456789012345678901234";
    private static final long PassTime=864000000L;

    public SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(SecretKey.getBytes(StandardCharsets.UTF_8));
    }


    public String generateToken(Long userId, String username, Integer role){
        return Jwts.builder()
                .claim("userId",userId)
                .claim("username", username)
                .claim("role", role)
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+PassTime))
                .signWith(getSecretKey(),Jwts.SIG.HS256)
                .compact();

    }

    public Claims extractClaims(String token){
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Long getUserId(String token){
        return extractClaims(token).get("userId",Long.class);
    }

    public String getUsername(String token){
        return extractClaims(token).getSubject();
    }
    

    public Integer getUserRole(String token){
        return extractClaims(token).get("role", Integer.class);
    }

    public boolean verify(String token){
        try{
            extractClaims(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
