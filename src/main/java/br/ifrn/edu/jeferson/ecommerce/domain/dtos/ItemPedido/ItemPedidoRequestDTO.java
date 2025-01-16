package br.ifrn.edu.jeferson.ecommerce.domain.dtos.ItemPedido;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(description = "Objeto para criar um novo ItemPedido")
public class ItemPedidoRequestDTO {

    @Schema(description = "Quantidade do produto no pedido", example = "2", required = true)
    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade deve ser maior ou igual a 1")
    private Integer quantidade;

    @Schema(description = "Valor unitário do produto", example = "99.90", required = true)
    @NotNull(message = "O valor unitário é obrigatório")
    @Min(value = 0, message = "O valor unitário deve ser maior ou igual a zero")
    private BigDecimal valorUnitario;

    @Schema(description = "ID do produto associado", example = "1", required = true)
    @NotNull(message = "O ID do produto é obrigatório")
    private Long produtoId;
}
