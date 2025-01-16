package br.ifrn.edu.jeferson.ecommerce.mapper;
import br.ifrn.edu.jeferson.ecommerce.domain.ItemPedido;
import br.ifrn.edu.jeferson.ecommerce.domain.Pedido;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.ItemPedido.ItemPedidoRequestDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.ItemPedido.ItemPedidoResponseDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Pedido.PedidoRequestDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Pedido.PedidoResponseDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemPedidoMapper {

    @Mapping(target="id", ignore=true)
    @Mapping(target="pedido", ignore=true)
    ItemPedido toEntity(ItemPedidoRequestDTO itemPedidoDTO);

    @Mapping(target = "pedido", source = "pedido.id")
    ItemPedidoResponseDTO toResponseDTO(ItemPedido itemPedido);

    @Mapping(target = "pedido", source = "pedido.id")
    List<ItemPedidoResponseDTO> toItemPedidosDTOList(List<ItemPedido> itemPedidos);

    @BeanMapping(nullValuePropertyMappingStrategy =  NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "produto", ignore = true)
    @Mapping(target="pedido", ignore=true)
    void updateEntityFromDTO(ItemPedidoRequestDTO pedidoDTO, @MappingTarget ItemPedido pedido);

}
