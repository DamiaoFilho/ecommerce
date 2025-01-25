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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger logger = LoggerFactory.getLogger(PedidoService.class);

    @Transactional
    public PedidoResponseDTO salvar(PedidoRequestDTO pedidoRequestDTO) {
        logger.info("Iniciando o processo de criação de pedido para o cliente com ID: {}", pedidoRequestDTO.getClienteId());

        Cliente cliente = clienteRepository.findById(pedidoRequestDTO.getClienteId())
                .orElseThrow(() -> {
                    logger.error("Cliente com ID {} não encontrado", pedidoRequestDTO.getClienteId());
                    return new ResourceNotFoundException("Cliente não encontrado");
                });

        logger.info("Cliente encontrado: {}", cliente.getNome());

        List<ItemPedido> itemPedidos = itemPedidoRepository.findAllById(pedidoRequestDTO.getItens());
        logger.info("Itens do pedido localizados. Quantidade de itens: {}", itemPedidos.size());

        Pedido pedido = pedidoMapper.toEntity(pedidoRequestDTO);
        pedido.setStatusPedido(StatusPedido.valueOf("AGUARDANDO"));
        pedido.setCliente(cliente);
        pedido.setValorTotal(BigDecimal.ZERO);

        logger.info("Pedido inicializado com status 'AGUARDANDO' e valor total inicial de 0");

        for (ItemPedido itemPedido : itemPedidos) {
            if (itemPedido.getPedido() == null) {
                logger.info("Processando item de pedido. ID: {}, Quantidade: {}, Valor unitário: {}",
                        itemPedido.getId(), itemPedido.getQuantidade(), itemPedido.getValorUnitario());

                pedido.getItens().add(itemPedido);

                BigDecimal totalValue = itemPedido.getValorUnitario().multiply(BigDecimal.valueOf(itemPedido.getQuantidade()));
                pedido.setValorTotal(pedido.getValorTotal().add(totalValue));
                itemPedido.setPedido(pedido);

                logger.info("Item adicionado ao pedido. ID do item: {}. Valor total do pedido atualizado para: {}",
                        itemPedido.getId(), pedido.getValorTotal());
            } else {
                logger.error("Item de pedido já possui pedido associado. ID do item: {}", itemPedido.getId());
                throw new BusinessException("Item de pedido já possui pedido. ID: " + itemPedido.getId());
            }
        }

        Pedido new_pedido = pedidoRepository.save(pedido);
        logger.info("Pedido salvo com sucesso no banco de dados. ID do pedido: {}", new_pedido.getId());

        return pedidoMapper.toResponseDTO(new_pedido);
    }

    @Cacheable(value = "pedidos")
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
        logger.info("Iniciando atualização do pedido com ID: {}", id);

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado"));

        StatusPedido novoStatus = StatusPedido.valueOf(status);

        logger.info("Validando transição de status. Status atual: {}, Novo status: {}", pedido.getStatusPedido(), novoStatus);

        StatusPedido statusAtual = pedido.getStatusPedido();

        switch (statusAtual) {
            case AGUARDANDO:
                if (novoStatus != StatusPedido.PAGO && novoStatus != StatusPedido.CANCELADO) {
                    logger.warn("Transição inválida de 'AGUARDANDO' para '{}'", novoStatus);
                    throw new IllegalStateException("A transição de status de 'AGUARDANDO' só pode ser para 'PAGO' ou 'CANCELADO'.");
                }
                break;

            case PAGO:
                if (novoStatus != StatusPedido.ENVIADO) {
                    logger.warn("Transição inválida de 'PAGO' para '{}'", novoStatus);
                    throw new IllegalStateException("A transição de status de 'PAGO' só pode ser para 'ENVIADO'.");
                }
                break;

            case ENVIADO:
                logger.warn("Tentativa de alterar status final 'ENVIADO'");
                throw new IllegalStateException("O status 'ENVIADO' é final e não pode ser alterado.");

            case CANCELADO:
                logger.warn("Tentativa de alterar status final 'CANCELADO'");
                throw new IllegalStateException("O status 'CANCELADO' é final e não pode ser alterado.");

            default:
                logger.error("Status atual inválido: {}", statusAtual);
                throw new IllegalStateException("Status atual inválido: " + statusAtual);
        }

        pedido.setStatusPedido(novoStatus);

        Pedido updated_pedido = pedidoRepository.save(pedido);

        return pedidoMapper.toResponseDTO(updated_pedido);
    }

    @Cacheable(value = "pedidosCliente")
    public Page<PedidoResponseDTO> listByCliente(Long id, Pageable pageable) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        Page<Pedido> pedidos = pedidoRepository.findByClienteId(id, pageable);

        return pedidos.map(pedidoMapper::toResponseDTO);
    }
}
