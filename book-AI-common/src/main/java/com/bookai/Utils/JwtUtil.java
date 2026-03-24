package com.bookai.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public class JwtUtil {
    private static final String SecretKey="my-secret-key-050425";
    private static final long PassTime=864000000L;

    public SecretKey getSecretKey(){
        return Keys.hmacShaKeyFor(SecretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Long userId,String username){
        return Jwts.builder()
                .claim("userId",userId)
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

}
