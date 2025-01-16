package br.ifrn.edu.jeferson.ecommerce.domain.dtos.ItemPedido;

import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Produto.ProdutoResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Schema(description = "Objeto para resposta de um ItemPedido")
public class ItemPedidoResponseDTO {

    @Schema(description = "ID do ItemPedido", example = "1")
    private Long id;

    @Schema(description = "Quantidade do produto no pedido", example = "2")
    private Integer quantidade;

    @Schema(description = "Valor unitário do produto", example = "99.90")
    private BigDecimal valorUnitario;

    @Schema(description = "ID do produto")
    private ProdutoResponseDTO produto;

    @Schema(description = "ID do pedido")
    private Long pedido;
}
