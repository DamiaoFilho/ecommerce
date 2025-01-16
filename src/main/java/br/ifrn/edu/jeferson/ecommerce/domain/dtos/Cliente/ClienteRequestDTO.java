package br.ifrn.edu.jeferson.ecommerce.domain.dtos.Cliente;

import br.ifrn.edu.jeferson.ecommerce.domain.Endereco;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Endereco.EnderecoRequestDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Endereco.EnderecoRequestNoClientDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteRequestDTO {

    @Valid
    @Schema(description = "Nome completo do cliente", example = "João Silva", required = true)
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Valid
    @Schema(description = "Email do cliente", example = "joao.silva@example.com", required = true)
    @Email(message = "Email inválido")
    @NotBlank(message = "Email é obrigatório")
    private String email;

    @Valid
    @Pattern(
            regexp = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}",
            message = "CPF deve estar no formato 123.456.789-09"
    )
    @Schema(description = "CPF do cliente", example = "123.456.789-00", required = true)
    @NotBlank(message = "CPF é obrigatório")
    private String cpf;

    @Valid
    @Schema(description = "Telefone do cliente", example = "(11) 98765-4321", required = true)
    @Size(min = 10, max = 15, message = "Telefone deve ter entre 10 e 15 caracteres")
    private String telefone;

    @Valid
    @Schema(description = "Endereço do Cliente")
    @NotNull(message = "Endereço é obrigatório")
    private EnderecoRequestNoClientDTO endereco;

}
