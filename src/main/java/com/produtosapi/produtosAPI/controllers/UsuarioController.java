package com.produtosapi.produtosAPI.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.produtosapi.produtosAPI.dto.UsuarioDTO;
import com.produtosapi.produtosAPI.models.Usuario;
import com.produtosapi.produtosAPI.services.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    
    @Operation(
        summary = "Cria um novo usuário.",
        description = "Cria um novo usuário no banco de dados.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Usuário criado com sucesso.",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = Usuario.class))),
        }
    )
    @PostMapping()
    public ResponseEntity<Usuario> postUsuario(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados do usuário para autenticação.",
            required = true,
            content = @Content(
                schema = @Schema(
                    implementation = UsuarioDTO.class
                )
            )
        )
        @Valid @RequestBody UsuarioDTO login
    ) {
        Usuario usuario = new Usuario();
        
        usuario.setUsername(login.getUsername());
        usuario.setPassword(login.getPassword());
        
        Usuario usuarioCriado = this.usuarioService.salvar(usuario);
        return ResponseEntity.ok(usuarioCriado);
    }
    
    
}
