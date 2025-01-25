package br.ifrn.edu.jeferson.ecommerce.service;

import br.ifrn.edu.jeferson.ecommerce.domain.Cliente;
import br.ifrn.edu.jeferson.ecommerce.domain.ItemPedido;
import br.ifrn.edu.jeferson.ecommerce.domain.Pedido;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Pedido.PedidoRequestDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Pedido.PedidoResponseDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.enums.StatusPedido;
import br.ifrn.edu.jeferson.ecommerce.exception.BusinessException;
import br.ifrn.edu.jeferson.ecommerce.exception.ResourceNotFoundException;
import br.ifrn.edu.jeferson.ecommerce.mapper.PedidoMapper;
import br.ifrn.edu.jeferson.ecommerce.repository.ClienteRepository;
import br.ifrn.edu.jeferson.ecommerce.repository.ItemPedidoRepository;
import br.ifrn.edu.jeferson.ecommerce.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PedidoMapper pedidoMapper;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional
    public PedidoResponseDTO salvar(PedidoRequestDTO pedidoRequestDTO) {
        Cliente cliente = clienteRepository.findById(pedidoRequestDTO.getClienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        List<ItemPedido> itemPedidos = itemPedidoRepository.findAllById(pedidoRequestDTO.getItens());

        Pedido pedido = pedidoMapper.toEntity(pedidoRequestDTO);
        pedido.setStatusPedido(StatusPedido.valueOf("AGUARDANDO"));
        pedido.setCliente(cliente);
        pedido.setValorTotal(new BigDecimal(0));

        for (ItemPedido itemPedido : itemPedidos) {
            if(itemPedido.getPedido() == null){
                pedido.getItens().add(itemPedido);

                BigDecimal total_value = itemPedido.getValorUnitario().multiply(BigDecimal.valueOf(itemPedido.getQuantidade()));
                pedido.setValorTotal(pedido.getValorTotal().add(total_value));
                itemPedido.setPedido(pedido);
            }else{
                throw new BusinessException("Item de pedido já possui pedido. ID: " + itemPedido.getId());
            }
        }

        Pedido new_pedido = pedidoRepository.save(pedido);
        return pedidoMapper.toResponseDTO(new_pedido);
    }

    public Page<PedidoResponseDTO> listar(Pageable pageable, Specification<Pedido> spec) {
        Page<Pedido> pedidos = pedidoRepository.findAll(spec, pageable);

        return pedidos.map(pedidoMapper::toResponseDTO);
    }

    public PedidoResponseDTO buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));

        return pedidoMapper.toResponseDTO(pedido);
    }

    public PedidoResponseDTO update(Long id, String status) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));

        StatusPedido novoStatus = StatusPedido.valueOf(status);

        StatusPedido statusAtual = pedido.getStatusPedido();

        switch (statusAtual) {
            case AGUARDANDO:
                if (novoStatus != StatusPedido.PAGO && novoStatus != StatusPedido.CANCELADO) {
                    throw new BusinessException("A transição de status de 'AGUARDANDO' só pode ser para 'PAGO' ou 'CANCELADO'.");
                }
                break;

            case PAGO:
                if (novoStatus != StatusPedido.ENVIADO) {
                    throw new BusinessException("A transição de status de 'PAGO' só pode ser para 'ENVIADO'.");
                }
                break;

            case ENVIADO:
                throw new BusinessException("O status 'ENVIADO' é final e não pode ser alterado.");

            case CANCELADO:
                throw new BusinessException("O status 'CANCELADO' é final e não pode ser alterado.");

            default:
                throw new BusinessException("Status atual inválido: " + statusAtual);
        }

        pedido.setStatusPedido(novoStatus);

        Pedido updated_pedido = pedidoRepository.save(pedido);

        return pedidoMapper.toResponseDTO(updated_pedido);
    }

    public Page<PedidoResponseDTO> listByCliente(Long id, Pageable pageable) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        Page<Pedido> pedidos = pedidoRepository.findByClienteId(id, pageable);

        return pedidos.map(pedidoMapper::toResponseDTO);
    }
}
