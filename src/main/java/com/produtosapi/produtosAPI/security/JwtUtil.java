package com.produtosapi.produtosAPI.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private long expirationTime;
    
    // Gera o token com base no username
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username) // define o "dono" do token
                .setIssuedAt(new Date()) // data de criação
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // data de expiração
                .signWith(this.getKeyBySecret()) // assina com a chave secreta
                .compact();
    }
    
    private SecretKey getKeyBySecret() {
        return Keys.hmacShaKeyFor(this.secret.getBytes());
    }

    // Pega o username de dentro do token
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // Valida se o token está correto e não expirou
    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.getKeyBySecret())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
