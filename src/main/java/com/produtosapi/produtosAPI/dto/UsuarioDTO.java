package com.produtosapi.produtosAPI.dto;

import com.produtosapi.produtosAPI.models.Usuario;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UsuarioDTO {
    
    @NotBlank
    private String username;
    
    @NotBlank
    private String password;
    
    public Usuario toUsuario() {
        Usuario usuario = new Usuario();
        
        usuario.setUsername(this.getUsername());
        usuario.setPassword(this.getPassword());
        
        return usuario;
    }
}
