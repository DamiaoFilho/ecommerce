package br.ifrn.edu.jeferson.ecommerce.controller;

import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Pedido.PedidoRequestDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Pedido.PedidoResponseDTO;
import br.ifrn.edu.jeferson.ecommerce.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pedidos")
@Tag(name = "Pedidos", description = "Gerenciamento de Pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Operation(summary = "Cadastrar novo pedido", description = "Cria um novo pedido para um cliente existente.")
    @PostMapping
    public ResponseEntity<PedidoResponseDTO> salvar(@RequestBody @Valid PedidoRequestDTO pedidoRequestDTO) {
        PedidoResponseDTO novoPedido = pedidoService.salvar(pedidoRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novoPedido);
    }

    @Operation(summary = "Listar todos os pedidos", description = "Retorna uma lista paginada de todos os pedidos.")
    @GetMapping
    public ResponseEntity<Page<PedidoResponseDTO>> listar(Pageable pageable) {
        Page<PedidoResponseDTO> pedidos = pedidoService.listar(pageable);
        return ResponseEntity.ok(pedidos);
    }

    @Operation(summary = "Buscar pedido por ID", description = "Retorna os detalhes de um pedido específico pelo ID.")
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> buscarPorId(@PathVariable Long id) {
        PedidoResponseDTO pedido = pedidoService.buscarPorId(id);
        return ResponseEntity.ok(pedido);
    }

    @Operation(summary = "Atualizar status do pedido", description = "Atualiza o status de um pedido específico.")
    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoResponseDTO> atualizarStatus(@PathVariable Long id, @RequestParam String status) {
        PedidoResponseDTO pedidoAtualizado = pedidoService.update(id, status);
        return ResponseEntity.ok(pedidoAtualizado);
    }

    @Operation(summary = "Listar pedidos por cliente", description = "Retorna uma lista paginada de pedidos associados a um cliente específico.")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<Page<PedidoResponseDTO>> listarPorCliente(@PathVariable Long clienteId, Pageable pageable) {
        Page<PedidoResponseDTO> pedidos = pedidoService.listByCliente(clienteId, pageable);
        return ResponseEntity.ok(pedidos);
    }
}
