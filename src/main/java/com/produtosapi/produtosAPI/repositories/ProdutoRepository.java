package com.produtosapi.produtosAPI.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.produtosapi.produtosAPI.models.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {}
