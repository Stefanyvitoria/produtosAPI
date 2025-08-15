package com.produtosapi.produtosAPI.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.produtosapi.produtosAPI.models.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
}

