package br.ifrn.edu.jeferson.ecommerce.controller;

import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Cliente.ClienteRequestDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Cliente.ClienteResponseDTO;
import br.ifrn.edu.jeferson.ecommerce.service.ClienteService;
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
@RequestMapping("/api/clientes")
@Tag(name = "Clientes", description = "Gerenciamento de clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    @Operation(summary = "Cadastrar cliente", description = "Cadastra um novo cliente no sistema")
    public ResponseEntity<ClienteResponseDTO> cadastrarCliente(@RequestBody @Valid ClienteRequestDTO clienteRequestDTO) {
        ClienteResponseDTO clienteCadastrado = clienteService.cadastrarCliente(clienteRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(clienteCadastrado);
    }

    @GetMapping
    @Operation(summary = "Listar clientes", description = "Retorna uma lista paginada de clientes")
    public ResponseEntity<Page<ClienteResponseDTO>> listarClientes(Pageable pageable) {
        Page<ClienteResponseDTO> clientes = clienteService.listarClientes(pageable);
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID", description = "Busca um cliente específico pelo ID")
    public ResponseEntity<ClienteResponseDTO> buscarClientePorId(@PathVariable Long id) {
        ClienteResponseDTO cliente = clienteService.buscarClientePorId(id);
        return ResponseEntity.ok(cliente);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar cliente", description = "Atualiza os dados de um cliente existente pelo ID")
    public ResponseEntity<ClienteResponseDTO> atualizarCliente(
            @PathVariable Long id,
            @RequestBody @Valid ClienteRequestDTO clienteRequestDTO
    ) {
        ClienteResponseDTO clienteAtualizado = clienteService.atualizarCliente(id, clienteRequestDTO);
        return ResponseEntity.ok(clienteAtualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar cliente", description = "Remove um cliente do sistema pelo ID")
    public ResponseEntity<Void> deletarCliente(@PathVariable Long id) {
        clienteService.deletarCliente(id);
        return ResponseEntity.noContent().build();
    }
}
