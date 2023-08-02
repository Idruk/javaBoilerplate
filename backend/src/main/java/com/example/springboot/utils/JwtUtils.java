package com.example.springboot.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtils {
    private static final String SECRET_KEY = "your_secret_key_herexxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";

    public static String generateJwt(String id) {
        long expirationTime = System.currentTimeMillis() + 3600000;
        Date expirationDate = new Date(expirationTime);

        Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        String jwt = Jwts.builder()
                .setSubject(id)
                .setExpiration(expirationDate)
                .signWith(key)
                .compact();

        return jwt;
    }

    public static boolean verifyJwt(String jwt) {
        try {
            Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt);

            Claims claims = claimsJws.getBody();

            Date expirationDate = claims.getExpiration();
            if (expirationDate == null || expirationDate.before(new Date())) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
