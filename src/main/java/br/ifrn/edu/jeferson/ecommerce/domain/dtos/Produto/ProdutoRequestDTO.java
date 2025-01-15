package br.ifrn.edu.jeferson.ecommerce.domain.dtos.Produto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para requisição de produto")
public class ProdutoRequestDTO {

    @Schema(description = "Nome do produto", example = "Notebook Dell")
    @NotBlank(message = "O nome do produto é obrigatório")
    @Valid
    private String nome;

    @Schema(description = "Descrição do produto", example = "Notebook de alta performance com 16GB RAM e 512GB SSD")
    private String descricao;

    @Schema(description = "Preço do produto", example = "2999.99")
    @NotNull(message = "O preço do produto é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser maior que zero")
    @Valid
    private BigDecimal preco;

    @Schema(description = "Quantidade em estoque", example = "50")
    @NotNull(message = "A quantidade em estoque é obrigatória")
    @Min(value = 0, message = "A quantidade em estoque deve ser igual ou maior que zero")
    @Valid
    private Integer estoque;

    @Schema(description = "IDs das categorias associadas ao produto")
    private List<Long> categoriasIds = new ArrayList<>();
}