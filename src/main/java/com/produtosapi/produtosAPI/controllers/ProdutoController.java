package com.produtosapi.produtosAPI.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.produtosapi.produtosAPI.models.Produto;
import com.produtosapi.produtosAPI.services.ProdutoService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/produtos")
public class ProdutoController {
    
    @Autowired
    private ProdutoService produtoService;
    
    
    @PostMapping()
    public ResponseEntity<Produto> postProdutos(@Valid @RequestBody Produto produto) {
        Produto produtoCriado = this.produtoService.salvar(produto);
        return ResponseEntity.ok(produtoCriado);
    }
    
    @GetMapping()
    public ResponseEntity<List<Produto>> getProdutos() {
        
        List<Produto> listProdutos = this.produtoService.listar();
        
        return ResponseEntity.ok(listProdutos);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Produto> getProdutoPorId( @Valid @PathVariable Long id ) {
        Produto produto = this.produtoService.ObterProdutoPorID(id);
        return ResponseEntity.ok(produto);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Produto> putProduto(@PathVariable Long id, @RequestBody Produto produto) {
        Produto produtoAtualizado = this.produtoService.atualizarProdutoPorID(id, produto);
        return ResponseEntity.ok(produtoAtualizado);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduto( @PathVariable Long id) {
        this.produtoService.deletarProduto(id);
        return ResponseEntity.noContent().build();
    }
}
