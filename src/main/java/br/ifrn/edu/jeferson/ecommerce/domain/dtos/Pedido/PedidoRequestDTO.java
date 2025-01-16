package br.ifrn.edu.jeferson.ecommerce.domain.dtos.Pedido;
import br.ifrn.edu.jeferson.ecommerce.domain.enums.StatusPedido;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Schema(description = "DTO para requisição de Pedido")
public class PedidoRequestDTO {

    @Schema(description = "ID do cliente que realizou o pedido", example = "1", required = true)
    @NotNull(message = "O ID do cliente é obrigatório")
    @Valid
    private Long clienteId;

    @Schema(description = "Status do pedido", example = "PENDENTE", required = true)
    private StatusPedido statusPedido;

    @Schema(description = "Itens do pedido")
    @NotNull(message = "Os itens do pedido são obrigatórios")
    @Size(min = 1, message = "O pedido deve conter pelo menos um item")
    @Valid
    private List<Long> itens;

}
