package br.ifrn.edu.jeferson.ecommerce.controller;

import br.ifrn.edu.jeferson.ecommerce.domain.dtos.ItemPedido.ItemPedidoRequestDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.ItemPedido.ItemPedidoResponseDTO;
import br.ifrn.edu.jeferson.ecommerce.service.ItemPedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/itens-pedidos")
@Tag(name = "Itens de Pedido", description = "Gerenciamento de itens de pedido")
public class ItemPedidoController {

    @Autowired
    private ItemPedidoService itemPedidoService;

    @Operation(summary = "Criar um novo item de pedido")
    @PostMapping
    public ResponseEntity<ItemPedidoResponseDTO> create(@RequestBody @Valid ItemPedidoRequestDTO itemPedidoRequestDTO) {
        ItemPedidoResponseDTO createdItem = itemPedidoService.create(itemPedidoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdItem);
    }

    @Operation(summary = "Listar todos os itens de pedido com paginação")
    @GetMapping
    public ResponseEntity<Page<ItemPedidoResponseDTO>> getAll(Pageable pageable) {
        Page<ItemPedidoResponseDTO> itensPedido = itemPedidoService.getAll(pageable);
        return ResponseEntity.ok(itensPedido);
    }

    @Operation(summary = "Buscar um item de pedido por ID")
    @GetMapping("/{id}")
    public ResponseEntity<ItemPedidoResponseDTO> getById(@PathVariable Long id) {
        ItemPedidoResponseDTO itemPedido = itemPedidoService.getById(id);
        return ResponseEntity.ok(itemPedido);
    }

    @Operation(summary = "Atualizar um item de pedido existente")
    @PutMapping("/{id}")
    public ResponseEntity<ItemPedidoResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid ItemPedidoRequestDTO itemPedidoRequestDTO
    ) {
        ItemPedidoResponseDTO updatedItem = itemPedidoService.update(id, itemPedidoRequestDTO);
        return ResponseEntity.ok(updatedItem);
    }

    @Operation(summary = "Deletar um item de pedido por ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        itemPedidoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
