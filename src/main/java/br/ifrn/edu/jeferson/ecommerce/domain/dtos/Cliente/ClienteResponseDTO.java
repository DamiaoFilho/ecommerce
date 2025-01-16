package br.ifrn.edu.jeferson.ecommerce.domain.dtos.Cliente;

import br.ifrn.edu.jeferson.ecommerce.domain.Endereco;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Endereco.EnderecoRequestNoClientDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Endereco.EnderecoResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteResponseDTO {

    @Schema(description = "ID do cliente", example = "1", required = true)
    private Long id;

    @Schema(description = "Nome completo do cliente", example = "João Silva", required = true)
    private String nome;

    @Schema(description = "Email do cliente", example = "joao.silva@example.com", required = true)
    private String email;

    @Schema(description = "CPF do cliente", example = "123.456.789-00", required = true)
    private String cpf;

    @Schema(description = "Telefone do cliente", example = "(11) 98765-4321", required = true)
    private String telefone;

    @Schema(description = "Endereço do cliente")
    private EnderecoRequestNoClientDTO endereco;

}
