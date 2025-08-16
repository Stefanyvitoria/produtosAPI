package com.produtosapi.produtosAPI.controllers;

import java.util.Map;

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
import com.produtosapi.produtosAPI.dto.UsuarioDTO;
import com.produtosapi.produtosAPI.security.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    
    @Operation(
        summary = "Autentica o usuário.",
        description = "Realiza a autenticação do usuário através da senha e nome de usuário.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Authenticação com sucesso",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AuthResponse.class)
                )
            ),
            @ApiResponse(
                responseCode = "401",
                description = "Authenticação Falhou.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Map.class)
                )
            )
        }
    )
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados do usuário para autenticação.",
            required = true,
            content = @Content(
                schema = @Schema(
                    implementation = UsuarioDTO.class
                )
            )
        )
        @RequestBody UsuarioDTO usuarioRequest
    ) {
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

