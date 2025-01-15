package br.ifrn.edu.jeferson.ecommerce.controller;

import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Produto.ProdutoRequestDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Produto.ProdutoResponseDTO;
import br.ifrn.edu.jeferson.ecommerce.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/produtos")
@Tag(name = "Produtos", description = "Endpoints para gerenciar produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @PostMapping
    @Operation(summary = "Salvar Produto", description = "Adiciona um novo produto ao sistema.")
    public ResponseEntity<ProdutoResponseDTO> salvar(@RequestBody @Valid ProdutoRequestDTO produtoDTO) {
        ProdutoResponseDTO produto = produtoService.salvar(produtoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(produto);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Produto por ID", description = "Retorna os detalhes de um produto com base no ID fornecido.")
    public ResponseEntity<ProdutoResponseDTO> buscarPorId(@PathVariable Long id) {
        ProdutoResponseDTO produto = produtoService.buscarPorId(id);
        return ResponseEntity.ok(produto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar Produto", description = "Atualiza todos os dados de um produto existente com base no ID fornecido.")
    public ResponseEntity<ProdutoResponseDTO> atualizar(@PathVariable Long id, @RequestBody @Valid ProdutoRequestDTO produtoDTO) {
        ProdutoResponseDTO produtoAtualizado = produtoService.update(id, produtoDTO);
        return ResponseEntity.ok(produtoAtualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Remover Produto", description = "Remove um produto do sistema com base no ID fornecido.")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        produtoService.remover(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/estoque")
    @Operation(summary = "Atualizar Estoque", description = "Atualiza a quantidade de estoque de um produto com base no ID fornecido.")
    public ResponseEntity<ProdutoResponseDTO> atualizarEstoque(@PathVariable Long id, @RequestParam Integer quantidade) {
        ProdutoResponseDTO produtoAtualizado = produtoService.atualizarEstoque(id, quantidade);
        return ResponseEntity.ok(produtoAtualizado);
    }

    @GetMapping("/categorias/{categoriaId}")
    @Operation(summary = "Listar Produtos por Categoria", description = "Lista todos os produtos associados a uma categoria específica.")
    public ResponseEntity<Page<ProdutoResponseDTO>> listarPorCategoria(
            @PathVariable Long categoriaId,
            Pageable pageable
    ) {
        Page<ProdutoResponseDTO> produtos = produtoService.listByCategorias(categoriaId, pageable);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping
    @Operation(summary = "Listar todos os produtos", description = "Lista todos os produtos.")
    public ResponseEntity<Page<ProdutoResponseDTO>> list(
            Pageable pageable
    ) {
        Page<ProdutoResponseDTO> produtos = produtoService.listAllProdutos(pageable);
        return ResponseEntity.ok(produtos);
    }
}
