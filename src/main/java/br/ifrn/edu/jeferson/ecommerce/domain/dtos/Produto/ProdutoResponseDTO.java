package br.ifrn.edu.jeferson.ecommerce.domain.dtos.Produto;

import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Categoria.CategoriaResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class ProdutoResponseDTO {

    @Schema(description = "ID do produto", example = "1")
    private Long id;

    @Schema(description = "Nome do produto", example = "Notebook Dell")
    private String nome;

    @Schema(description = "Descrição do produto", example = "Notebook de alta performance com 16GB RAM e 512GB SSD")
    private String descricao;

    @Schema(description = "Preço do produto", example = "2999.99")
    private BigDecimal preco;

    @Schema(description = "Quantidade em estoque", example = "50")
    private Integer estoque;

    @Schema(description = "Categorias associadas ao produto")
    private List<CategoriaResponseDTO> categorias = new ArrayList<>();
}