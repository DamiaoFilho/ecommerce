package br.ifrn.edu.jeferson.ecommerce.service;

import br.ifrn.edu.jeferson.ecommerce.domain.ItemPedido;
import br.ifrn.edu.jeferson.ecommerce.domain.Produto;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.ItemPedido.ItemPedidoRequestDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.ItemPedido.ItemPedidoResponseDTO;
import br.ifrn.edu.jeferson.ecommerce.exception.BusinessException;
import br.ifrn.edu.jeferson.ecommerce.exception.ResourceNotFoundException;
import br.ifrn.edu.jeferson.ecommerce.mapper.ItemPedidoMapper;
import br.ifrn.edu.jeferson.ecommerce.repository.ItemPedidoRepository;
import br.ifrn.edu.jeferson.ecommerce.repository.ProdutoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class ItemPedidoService {

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ItemPedidoMapper itemPedidoMapper;

    private static final Logger logger = LoggerFactory.getLogger(ItemPedidoService.class);

    @Transactional
    public ItemPedidoResponseDTO create(ItemPedidoRequestDTO itemPedidoRequestDTO) {
        logger.info("Iniciando criação de ItemPedido para Produto ID: {} e Quantidade: {}",
                itemPedidoRequestDTO.getProdutoId(), itemPedidoRequestDTO.getQuantidade());

        Produto produto = produtoRepository.findById(itemPedidoRequestDTO.getProdutoId())
                .orElseThrow(() -> {
                    logger.error("Produto com ID {} não encontrado", itemPedidoRequestDTO.getProdutoId());
                    return new ResourceNotFoundException("Produto não encontrado");
                });

        System.out.println("OOOOPPAAA");
        logger.info("Produto encontrado: {}. Estoque atual: {}", produto.getNome(), produto.getEstoque());

        if (produto.getEstoque() < itemPedidoRequestDTO.getQuantidade()) {
            logger.error("Produto com ID {} não possui quantidade suficiente em estoque. Estoque disponível: {}, Quantidade solicitada: {}",
                    produto.getId(), produto.getEstoque(), itemPedidoRequestDTO.getQuantidade());
            throw new BusinessException("Produto não possui quantidade em estoque");
        }

        ItemPedido itemPedido = itemPedidoMapper.toEntity(itemPedidoRequestDTO);
        itemPedido.setProduto(produto);
        produto.setEstoque(produto.getEstoque() - itemPedido.getQuantidade());

        logger.info("ItemPedido criado. Produto ID: {}, Quantidade: {}, Estoque atualizado: {}",
                produto.getId(), itemPedido.getQuantidade(), produto.getEstoque());

        ItemPedido savedItemPedido = itemPedidoRepository.save(itemPedido);
        produtoRepository.save(produto);

        logger.info("ItemPedido salvo com sucesso no banco de dados. ID do ItemPedido: {}", savedItemPedido.getId());
        return itemPedidoMapper.toResponseDTO(savedItemPedido);
    }

    public Page<ItemPedidoResponseDTO> getAll(Pageable pageable) {
        Page<ItemPedido> itensPedido = itemPedidoRepository.findAll(pageable);
        return itensPedido.map(itemPedidoMapper::toResponseDTO);
    }

    public ItemPedidoResponseDTO getById(Long id) {
        ItemPedido itemPedido = itemPedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item do Pedido não encontrado"));

        return itemPedidoMapper.toResponseDTO(itemPedido);
    }

    @Transactional
    public ItemPedidoResponseDTO update(Long id, ItemPedidoRequestDTO itemPedidoRequestDTO) {
        ItemPedido itemPedido = itemPedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item do Pedido não encontrado"));

        Produto produto = produtoRepository.findById(itemPedidoRequestDTO.getProdutoId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        itemPedidoMapper.updateEntityFromDTO(itemPedidoRequestDTO, itemPedido);
        itemPedido.setProduto(produto);

        ItemPedido updatedItemPedido = itemPedidoRepository.save(itemPedido);

        return itemPedidoMapper.toResponseDTO(updatedItemPedido);
    }

    @Transactional
    public void delete(Long id) {
        ItemPedido itemPedido = itemPedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item do Pedido não encontrado"));

        itemPedidoRepository.delete(itemPedido);
    }
}
