package com.produtosapi.produtosAPI.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.produtosapi.produtosAPI.models.Usuario;
import com.produtosapi.produtosAPI.repositories.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    
    public Usuario salvar(Usuario usuario) {
        final String pass = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(pass);
        return this.usuarioRepository.save(usuario);
    }
    
    public boolean autenticar(Usuario loginRequest) {
        return usuarioRepository.findByUsername(loginRequest.getUsername())
                .map(usuario -> passwordEncoder.matches(loginRequest.getPassword(), usuario.getPassword()))
                .orElse(false);
    }
}
