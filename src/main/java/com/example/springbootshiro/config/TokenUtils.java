package com.example.springbootshiro.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

import java.util.Date;

/**
 * @author Amin
 * @Create 2021-04-21 20:38
 */
public class TokenUtils {
    private static final String secret = "secret";
    public static final String tokenHeard = "tokenHead";
    private static final Long expTime = 60 * 5 * 1000L;

    public static String getToken(String name,String id,String ip) {
        JwtBuilder builder = Jwts.builder();
        //HS256 SignatureAlgorithm.HS256
        builder.signWith(io.jsonwebtoken.SignatureAlgorithm.HS256,secret);
        builder.setId(id).setSubject(name).setAudience(ip);
        builder.setIssuedAt(new Date()).setExpiration(new Date(System.currentTimeMillis() + expTime));
        String token = builder.compact();
        return token;
    }

    public static Claims getTokenBody(String token) {
        JwtParser parser = Jwts.parser();
        Claims body = parser.setSigningKey(secret).parseClaimsJws(token).getBody();
        return body;
    }

    public static String getName(String token) {
        Claims body = getTokenBody(token);
        String id = body.getId();
        return id;
    }
}
