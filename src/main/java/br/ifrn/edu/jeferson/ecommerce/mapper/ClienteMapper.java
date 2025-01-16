package br.ifrn.edu.jeferson.ecommerce.mapper;

import br.ifrn.edu.jeferson.ecommerce.domain.Categoria;
import br.ifrn.edu.jeferson.ecommerce.domain.Cliente;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Categoria.CategoriaRequestDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Categoria.CategoriaResponseDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Cliente.ClienteRequestDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Cliente.ClienteResponseDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    ClienteResponseDTO toResponseDTO(Cliente cliente);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pedidos", ignore = true)
    @Mapping(target = "endereco", ignore = true)
    Cliente toEntity(ClienteRequestDTO dto);

    List<ClienteResponseDTO> toDTOList(List<Cliente> clientes);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pedidos", ignore = true)
    @Mapping(target = "endereco", ignore = true)
    void updateEntityFromDTO(ClienteRequestDTO dto, @MappingTarget Cliente cliente);
}
