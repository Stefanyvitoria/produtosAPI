package com.produtosapi.produtosAPI.models;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "produtos")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Produto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100, unique = true)
    @NotBlank(message = "nome é obrigatório")
    private String nome;
    
    @Column(length = 500)
    private String descricao;
    
    @Column(nullable = false)
    @Positive(message = "preco deve ser maior que zero")
    private Double preco;
    
    @Column(name = "quantidade_estoque", nullable = false)
    @Min(value = 0, message = "quantidadeEstoque não pode ser negativo")
    private Integer quantidadeEstoque;
    
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;
    
    @PrePersist
    public void prePersistData() {
        this.dataCriacao = LocalDateTime.now();
    }
}
