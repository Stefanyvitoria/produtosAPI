package com.produtosapi.produtosAPI.dto;

import com.produtosapi.produtosAPI.models.Produto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProdutoUpdateDTO {
    private String nome;
    private String descricao;
    private Double preco;
    private Integer quantidadeEstoque;
    
    // Colocar no service
    public Produto toProduto() {
        Produto produto = new Produto();
        
        produto.setNome(this.getNome());
        produto.setDescricao(this.getDescricao());
        produto.setPreco(this.getPreco());
        produto.setQuantidadeEstoque(this.getQuantidadeEstoque());
        
        return produto;
    }
}

