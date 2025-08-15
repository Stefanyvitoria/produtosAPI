package com.produtosapi.produtosAPI.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.produtosapi.produtosAPI.models.Usuario;
import com.produtosapi.produtosAPI.services.UsuarioService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Usuario usuario) {
        boolean autenticado = usuarioService.autenticar(usuario);

        if (autenticado) {
            return ResponseEntity.ok("Login realizado com sucesso!");
        } else {
            return ResponseEntity.status(401).body("Usuário ou senha inválidos");
        }
    }
}

