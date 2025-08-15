package com.produtosapi.produtosAPI.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.produtosapi.produtosAPI.models.Usuario;
import com.produtosapi.produtosAPI.services.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    @PostMapping()
    public ResponseEntity<Usuario> postUsuario(@Valid @RequestBody Usuario usuario) {
        Usuario usuarioCriado = this.usuarioService.salvar(usuario);
        return ResponseEntity.ok(usuarioCriado);
    }
    
    
}
