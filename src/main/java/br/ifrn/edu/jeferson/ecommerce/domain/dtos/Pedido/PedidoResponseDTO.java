package br.ifrn.edu.jeferson.ecommerce.domain.dtos.Pedido;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Cliente.ClienteResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Schema(description = "DTO para resposta de Pedido")
public class PedidoResponseDTO {

    @Schema(description = "ID do pedido", example = "1")
    private Long id;

    @Schema(description = "Data do pedido", example = "2025-01-15T14:30:00")
    private LocalDateTime dataPedido;

    @Schema(description = "Valor total do pedido", example = "150.00")
    private BigDecimal valorTotal;

    @Schema(description = "Status do pedido", example = "PENDENTE")
    private String statusPedido;

    @Schema(description = "Cliente associado ao pedido")
    private ClienteResponseDTO cliente;

    @Schema(description = "Itens do pedido")
    private List<Long> itens;
}
