package br.ifrn.edu.jeferson.ecommerce.mapper;
import br.ifrn.edu.jeferson.ecommerce.domain.ItemPedido;
import br.ifrn.edu.jeferson.ecommerce.domain.Pedido;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Pedido.PedidoRequestDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Pedido.PedidoResponseDTO;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PedidoMapper {

    @Mapping(target="id", ignore=true)
    @Mapping(target="itens", ignore = true)
    @Mapping(target="cliente", ignore = true)
    @Mapping(target = "valorTotal", ignore = true)
    Pedido toEntity(PedidoRequestDTO pedidoDTO);

    @Mapping(target = "itens", source = "itens")
    PedidoResponseDTO toResponseDTO(Pedido pedido);

    List<PedidoResponseDTO> toPedidosDTOList(List<Pedido> pedidos);

    @BeanMapping(nullValuePropertyMappingStrategy =  NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    @Mapping(target = "itens", ignore = true)
    @Mapping(target = "valorTotal", ignore = true)
    void updateEntityFromDTO(PedidoRequestDTO pedidoDTO, @MappingTarget Pedido pedido);

    default List<Long> mapItens(List<ItemPedido> itens) {
        if (itens == null) {

        }
        return itens.stream()
                .map(ItemPedido::getId)
                .collect(Collectors.toList());
    }
}
