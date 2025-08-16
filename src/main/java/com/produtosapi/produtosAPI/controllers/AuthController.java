package com.produtosapi.produtosAPI.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.produtosapi.produtosAPI.dto.AuthResponse;
import com.produtosapi.produtosAPI.models.Usuario;
import com.produtosapi.produtosAPI.security.JwtUtil;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody Usuario usuarioRequest) {
        try {
            // Autentica usuário no banco com a senha e username
            // Se falhar, lança AuthenticationException
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    usuarioRequest.getUsername(),
                    usuarioRequest.getPassword()
                )
            );
            
            String token = jwtUtil.generateToken(usuarioRequest.getUsername());
            
            return ResponseEntity.ok(new AuthResponse(token));
            
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).build();
        }
    }
}

