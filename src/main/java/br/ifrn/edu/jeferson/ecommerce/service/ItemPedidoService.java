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

    @Transactional
    public ItemPedidoResponseDTO create(ItemPedidoRequestDTO itemPedidoRequestDTO) {
        Produto produto = produtoRepository.findById(itemPedidoRequestDTO.getProdutoId())
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        if(produto.getEstoque() < itemPedidoRequestDTO.getQuantidade()){
            throw new BusinessException("Produto não possui quantidade em estoque");
        }

        ItemPedido itemPedido = itemPedidoMapper.toEntity(itemPedidoRequestDTO);
        itemPedido.setProduto(produto);
        produto.setEstoque(produto.getEstoque() - itemPedido.getQuantidade());

        ItemPedido savedItemPedido = itemPedidoRepository.save(itemPedido);
        produtoRepository.save(produto);

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
