package com.produtosapi.produtosAPI.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.produtosapi.produtosAPI.models.Produto;
import com.produtosapi.produtosAPI.repositories.ProdutoRepository;

import com.produtosapi.produtosAPI.enums.ordemEnum;

@Service
public class ProdutoService {
    
    @Autowired
    private ProdutoRepository produtoRepository;
    
    
    public Produto salvar(Produto produto) {
        return this.produtoRepository.save(produto);
    }
    
    public List<Produto> listar(String nome, String ordemStr) throws NotFoundException {
        Sort sort = null;
        
        if (ordemStr != null && !ordemStr.isBlank()) { // com ordenação
            ordemEnum ordem = ordemEnum.fromString(ordemStr);
            sort = (ordem == ordemEnum.DESC) ? Sort.by("preco").descending() : Sort.by("preco").ascending();
        }
        
        List<Produto> produtos;
        
        if (nome != null && !nome.isBlank()) { // Filtra pelo nome
            
            if (sort != null) { // com ordenação
                produtos = produtoRepository.findByNomeContainingIgnoreCase(nome, sort);
            } else { // sem ordenação
                produtos = produtoRepository.findByNomeContainingIgnoreCase(nome);
            }
            
        } else { // Sem filtro
            
            if (sort != null) { // com ordenação
                produtos = produtoRepository.findAll(sort);
            } else { // sem ordenação
                produtos = produtoRepository.findAll();
            }
            
        }
        
        if (produtos.isEmpty()) {
            throw new NotFoundException();
        }
        
        return produtos;
    }
    
    public Produto obterProdutoPorID(Long id) throws NotFoundException {
        return this.produtoRepository.findById(id)
            .orElseThrow(() -> new NotFoundException());
    }
    
    public Produto atualizarProdutoPorID(Long id ,Produto produtoNovo) throws NotFoundException {
        Produto produto = this.obterProdutoPorID(id);
        
        if (produtoNovo.getNome() != null) produto.setNome(produtoNovo.getNome());
        if (produtoNovo.getDescricao() != null) produto.setDescricao(produtoNovo.getDescricao());
        if (produtoNovo.getPreco() != null) produto.setPreco(produtoNovo.getPreco());
        if (produtoNovo.getQuantidadeEstoque() != null) produto.setQuantidadeEstoque(produtoNovo.getQuantidadeEstoque());
        
        return this.salvar(produto);
    }
    
    public void deletarProduto(Long id) throws NotFoundException {
        this.obterProdutoPorID(id);
        this.produtoRepository.deleteById(id);
    }
    
}
