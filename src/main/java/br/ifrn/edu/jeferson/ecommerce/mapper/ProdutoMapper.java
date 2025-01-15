package br.ifrn.edu.jeferson.ecommerce.mapper;

import br.ifrn.edu.jeferson.ecommerce.domain.Endereco;
import br.ifrn.edu.jeferson.ecommerce.domain.Produto;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Endereco.EnderecoRequestDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Endereco.EnderecoResponseDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Produto.ProdutoRequestDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Produto.ProdutoResponseDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {
    @Mapping(target="categorias", ignore = true)
    Produto toEntity(ProdutoRequestDTO produtoDto);

    @Mapping(target = "categorias", source = "categorias")
    ProdutoResponseDTO toResponseDTO(Produto produto);

    List<ProdutoResponseDTO> toProdutosDTOList(List<Produto> produtos);

    @BeanMapping(nullValuePropertyMappingStrategy =  NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categorias", ignore = true)
    void updateEntityFromDTO(ProdutoRequestDTO produtoDto, @MappingTarget Produto produto);
}
