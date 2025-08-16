package com.produtosapi.produtosAPI.dto;

import com.produtosapi.produtosAPI.models.Produto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProdutoDTO {
    
    @NotBlank
    private String nome;
    
    private String descricao;
    
    @NotNull
    @Positive(message = "preco deve ser maior que zero")
    private Double preco;
    
    @NotNull
    @Min(value = 0, message = "quantidadeEstoque não pode ser negativo")
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
