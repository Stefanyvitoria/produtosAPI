package com.produtosapi.produtosAPI.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.produtosapi.produtosAPI.models.Produto;
import com.produtosapi.produtosAPI.repositories.ProdutoRepository;


@Service
public class ProdutoService {
    
    @Autowired
    private ProdutoRepository produtoRepository;
    
    
    public Produto salvar(Produto produto) {
        return this.produtoRepository.save(produto);
    }
    
    public List<Produto> listar(String nome) {
        
        List<Produto> listaProdutos;
        
        if (nome != null && !nome.isBlank() ) {
            listaProdutos = this.produtoRepository.findByNomeContainingIgnoreCase(nome);
        } else {
            listaProdutos = this.produtoRepository.findAll();
        }
        return listaProdutos;
        
    }
    
    public Produto ObterProdutoPorID(Long id) {
        Produto produto = this.produtoRepository.getReferenceById(id);
        return produto;
    }
    
    public Produto atualizarProdutoPorID(Long id ,Produto produtoNovo) {
        Produto produto = this.ObterProdutoPorID(id);
        
        if (produtoNovo.getNome() != null) produto.setNome(produtoNovo.getNome());
        if (produtoNovo.getDescricao() != null) produto.setDescricao(produtoNovo.getDescricao());
        if (produtoNovo.getPreco() != null) produto.setPreco(produtoNovo.getPreco());
        if (produtoNovo.getQuantidadeEstoque() != null) produto.setQuantidadeEstoque(produtoNovo.getQuantidadeEstoque());
        
        return this.salvar(produto);
    }
    
    public void deletarProduto(Long id) {
        this.produtoRepository.deleteById(id);
    }
}
