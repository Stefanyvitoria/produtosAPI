package com.produtosapi.produtosAPI.controllers;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.produtosapi.produtosAPI.dto.ProdutoDTO;
import com.produtosapi.produtosAPI.dto.ProdutoUpdateDTO;
import com.produtosapi.produtosAPI.models.Produto;
import com.produtosapi.produtosAPI.services.ProdutoService;
import com.produtosapi.produtosAPI.security.SecurityConfig;

import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/produtos")
@Tag(name = "Produtos", description = "API para gerenciamento de produtos")
@SecurityRequirement(name = SecurityConfig.SECURITY) // Exige autenticação
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @Operation(
        summary = "Criar produto",
        description = "Cria um novo produto no banco. Validações: nome (obrigatório), preço (> 0), estoque (≥ 0).",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Produto criado com sucesso.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Produto.class)
            )),
            @ApiResponse(
                responseCode = "400",
                description = "Dados inválidos. Retorna erros de validação por campo.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = Map.class
                    )
                )
            )
        }
    )
    @PostMapping()
    public ResponseEntity<Produto> postProdutos(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados do produto para criação.",
            required = true,
            content = @Content(
                schema = @Schema(
                    implementation = ProdutoDTO.class
                )
            )
        )
        @Valid @RequestBody ProdutoDTO produtoRequest
    ) {
        Produto produtoCriado = this.produtoService.salvar(produtoRequest.toProduto());
        
        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(produtoCriado.getId())
            .toUri();

        return ResponseEntity.created(location).body(produtoCriado);
    }
    
    
    @Operation(
        summary = "Listar produtos",
        description = "Retorna uma lista de produtos com opções de filtro e ordenação",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Lista de produtos retornada com sucesso.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = Produto[].class
                    )
                )
            ),
            @ApiResponse(
                responseCode = "204", // TODO: Tratar o caso de não vir nenhum produto
                description = "Nenhum produto encontrado para os filtros aplicados",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = Map.class
                    )
                )
            )
        }
    )
    @GetMapping()
    public ResponseEntity<List<Produto>> getProdutos(
        @Parameter(
            description = "Filtro por nome do produto (case insensitive)" // Validar case insensitive
        )
        @RequestParam(required = false) String nome,
        @Parameter(
            description = "Ordenação por preço: 'asc' para crescente ou 'desc' para decrescente"
        )
        @RequestParam(required = false) String ordemPreco
    ) {
        List<Produto> listProdutos = this.produtoService.listar(nome, ordemPreco);
        return ResponseEntity.ok(listProdutos);
    }
    
    
    @Operation(
        summary = "Buscar produto por ID",
        description = "Recupera o produto com ID especificado.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Produto encontrado e retornado com sucesso.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = Produto.class
                    )
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Produto não encontrado para o ID informado.", // TODO: fazer o tratamento
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = Map.class
                    )
                )
            )
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Produto> getProdutoPorId(
        @Parameter(
            description = "ID do produto a ser buscado."
        )
        @PathVariable Long id) {
        Produto produto = this.produtoService.ObterProdutoPorID(id);
        return ResponseEntity.ok(produto);
    }
    
    
    @Operation(
        summary = "Atualizar produto",
        description = "Atualiza um produto existente. Validações: preço (> 0), estoque (≥ 0).",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Produto atualizado com sucesso.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = Produto.class
                    )
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Dados inválidos na requisição.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = Map.class
                    )
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Produto não encontrado para o ID informado",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = Map.class
                    )
                )
            )
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Produto> putProduto(
        @Parameter(
            description = "ID do produto a ser atualizado."
        )
        @PathVariable Long id,
        
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Dados do produto para atualização",
            required = true,
            content = @Content(
                schema = @Schema(
                    implementation = ProdutoUpdateDTO.class
                )
            )
        )
        @RequestBody ProdutoUpdateDTO produtoRequest
    ) {
        Produto produtoAtualizado = this.produtoService.atualizarProdutoPorID(id, produtoRequest.toProduto());
        return ResponseEntity.ok(produtoAtualizado);
    }

    @Operation(
        summary = "Excluir produto",
        description = "Deleta o produto especificado.",
        responses = {
            @ApiResponse(
                responseCode = "204",
                description = "Produto excluído com sucesso.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = Map.class
                    )
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Produto não encontrado para o ID informado.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = Map.class
                    )
                )
            )
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduto(
        @Parameter(
            description = "ID do produto a ser excluído"
        )
        @PathVariable Long id) {
        this.produtoService.deletarProduto(id);
        return ResponseEntity.noContent().build();
    }
}
