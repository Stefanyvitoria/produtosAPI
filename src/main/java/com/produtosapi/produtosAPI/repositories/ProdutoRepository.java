package com.produtosapi.produtosAPI.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.produtosapi.produtosAPI.models.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
    
    // Busca todos produtos que contenha a string passada de forma case insensitive
    List<Produto> findByNomeContainingIgnoreCase(String nome);
}
