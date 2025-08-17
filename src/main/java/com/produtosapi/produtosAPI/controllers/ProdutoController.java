package com.produtosapi.produtosAPI.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
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
import com.produtosapi.produtosAPI.dto.ResponseAPI;
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
                    schema = @Schema(implementation = ResponseAPI.class)
            )),
            @ApiResponse(
                responseCode = "400",
                description = "Dados inválidos. Retorna erros de validação por campo.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = ResponseAPI.class
                    )
                )
            ),
            @ApiResponse(
                responseCode = "409",
                description = "Conflito de produtos.",
                content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = ResponseAPI.class))
            )
        }
    )
    @PostMapping()
    public ResponseEntity<ResponseAPI<Produto>> postProdutos(
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
        ResponseAPI<Produto> responseAPI = new ResponseAPI<>();
        
        try {
            Produto produtoCriado = this.produtoService.salvar(produtoRequest.toProduto());
            
            URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(produtoCriado.getId())
                .toUri();

            responseAPI.setCode(HttpStatus.CREATED.value());
            responseAPI.setStatus("sucesso");
            responseAPI.setMessage("Produto criado com sucesso!");
            responseAPI.setData(produtoCriado);
            
            return ResponseEntity.created(location).body(responseAPI);
        
        } catch (DataIntegrityViolationException e) {
            responseAPI.setCode(HttpStatus.CONFLICT.value());
            responseAPI.setStatus("erro");
            responseAPI.setMessage("Produto já existe no banco!");
            
            return ResponseEntity.status(HttpStatus.CONFLICT.value()).body(responseAPI);
        }
        
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
                        implementation = ResponseAPI.class
                    )
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Nenhum produto encontrado para os filtros aplicados.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = ResponseAPI.class
                    )
                )
            )
        }
    )
    @GetMapping()
    public ResponseEntity<ResponseAPI<List<Produto>>> getProdutos(
        @Parameter(
            description = "Filtro por nome do produto (case insensitive)" // Validar case insensitive
        )
        @RequestParam(required = false) String nome,
        @Parameter(
            description = "Ordenação por preço: 'asc' para crescente ou 'desc' para decrescente"
        )
        @RequestParam(required = false) String ordemPreco
    ) {
        ResponseAPI<List<Produto>> responseAPI = new ResponseAPI<>();
        
        try {
            List<Produto> listProdutos = this.produtoService.listar(nome, ordemPreco);
            
            responseAPI.setCode(HttpStatus.OK.value());
            responseAPI.setStatus("sucesso");
            responseAPI.setMessage("Consulta realizada com sucesso!");
            responseAPI.setData(listProdutos);
            
            return ResponseEntity.ok().body(responseAPI);
            
        } catch (NotFoundException e) {
            
            responseAPI.setCode(HttpStatus.NOT_FOUND.value());
            responseAPI.setStatus("erro");
            responseAPI.setMessage("Nenhum produto encontrado!");
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(responseAPI);
        }

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
                        implementation = ResponseAPI.class
                    )
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Produto não encontrado para o ID informado.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = ResponseAPI.class
                    )
                )
            )
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ResponseAPI<Produto>> getProdutoPorId(
        @Parameter(
            description = "ID do produto a ser buscado."
        )
        @PathVariable Long id
    ) {
        ResponseAPI<Produto> responseAPI = new ResponseAPI<>();
        
        try {
            Produto produto = this.produtoService.obterProdutoPorID(id);
            
            responseAPI.setCode(HttpStatus.OK.value());
            responseAPI.setStatus("sucesso");
            responseAPI.setMessage("Consulta realizada com sucesso!");
            responseAPI.setData(produto);
            
            return ResponseEntity.ok().body(responseAPI);
            
        } catch (NotFoundException e) {
            
            responseAPI.setCode(HttpStatus.NOT_FOUND.value());
            responseAPI.setStatus("erro");
            responseAPI.setMessage("Produto não encontrado!");
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(responseAPI);
        }
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
                        implementation = ResponseAPI.class
                    )
                )
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Dados inválidos na requisição.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = ResponseAPI.class
                    )
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Produto não encontrado para o ID especificado.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = ResponseAPI.class
                    )
                )
            )
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ResponseAPI<Produto>> putProduto(
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
        @Valid @RequestBody ProdutoUpdateDTO produtoRequest
    ) {
        ResponseAPI<Produto> responseAPI = new ResponseAPI<>();
        
        try {
            Produto produtoAtualizado = this.produtoService.atualizarProdutoPorID(id, produtoRequest.toProduto());
            
            responseAPI.setCode(HttpStatus.OK.value());
            responseAPI.setStatus("sucesso");
            responseAPI.setMessage("Produto atualizado com sucesso!");
            responseAPI.setData(produtoAtualizado);
            
            return ResponseEntity.ok().body(responseAPI);
            
        } catch (NotFoundException e) {
            
            responseAPI.setCode(HttpStatus.NOT_FOUND.value());
            responseAPI.setStatus("erro");
            responseAPI.setMessage("Produto não encontrado!");
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(responseAPI);
            
        }
    }
    
    
    @Operation(
        summary = "Excluir produto",
        description = "Deleta o produto especificado.",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "Produto excluído com sucesso.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = ResponseAPI.class
                    )
                )
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Produto não encontrado para o ID informado.",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(
                        implementation = ResponseAPI.class
                    )
                )
            )
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseAPI<Void>> deleteProduto(
        @Parameter(
            description = "ID do produto a ser excluído"
        )
        @PathVariable Long id
    ) {
        ResponseAPI<Void> responseAPI = new ResponseAPI<>();
        
        try {
            this.produtoService.deletarProduto(id);
            
            responseAPI.setCode(HttpStatus.OK.value());
            responseAPI.setStatus("sucesso");
            responseAPI.setMessage("Exclusão realizada com sucesso!");
            
            return ResponseEntity.ok().body(responseAPI);
            
        } catch (NotFoundException e) {
            
            responseAPI.setCode(HttpStatus.NOT_FOUND.value());
            responseAPI.setStatus("erro");
            responseAPI.setMessage("Produto não encontrado!");
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(responseAPI);
        }
    }
    
}
