package com.produtosapi.produtosAPI.dto;

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
}
