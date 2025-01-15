package br.ifrn.edu.jeferson.ecommerce.service;


import br.ifrn.edu.jeferson.ecommerce.domain.Categoria;
import br.ifrn.edu.jeferson.ecommerce.domain.Produto;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Produto.ProdutoRequestDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Produto.ProdutoResponseDTO;
import br.ifrn.edu.jeferson.ecommerce.exception.ResourceNotFoundException;
import br.ifrn.edu.jeferson.ecommerce.mapper.ProdutoMapper;
import br.ifrn.edu.jeferson.ecommerce.repository.CategoriaRepository;
import br.ifrn.edu.jeferson.ecommerce.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProdutoMapper produtoMapper;

    @Transactional
    public ProdutoResponseDTO salvar(ProdutoRequestDTO produtoDTO) {
        Produto produto = produtoMapper.toEntity(produtoDTO);
        List<Categoria> categorias = categoriaRepository.findAllById(produtoDTO.getCategoriasIds());
        produto.setCategorias(categorias);
        Produto saved_produto = produtoRepository.save(produto);
        return produtoMapper.toResponseDTO(saved_produto);
    }

    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto Não Encontrado"));
        return produtoMapper.toResponseDTO(produto);
    }

    public ProdutoResponseDTO update(Long id, ProdutoRequestDTO produtoDTO) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto Não encontrado"));

        List<Categoria> categorias = categoriaRepository.findAllById(produtoDTO.getCategoriasIds());

        produtoMapper.updateEntityFromDTO(produtoDTO, produto);
        Produto updated_produto = produtoRepository.save(produto);

        return produtoMapper.toResponseDTO(updated_produto);
    }

    public void remover(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto Não Encontrado"));

        produtoRepository.deleteById(produto.getId());
    }

    public ProdutoResponseDTO atualizarEstoque(Long id, int estoque) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto Não Encontrado"));

        produto.setEstoque(estoque + produto.getEstoque());
        Produto updated_produto = produtoRepository.save(produto);
        return produtoMapper.toResponseDTO(updated_produto);
    }

    public Page<ProdutoResponseDTO> listByCategorias(Long id, Pageable pageable) {
        Page<Produto> produtos = produtoRepository.findByCategoriasId(id, pageable);
        return produtos.map(produtoMapper::toResponseDTO);
    };

    public Page<ProdutoResponseDTO> listAllProdutos(Pageable pageable) {
        Page<Produto> produtos = produtoRepository.findAll(pageable);
        return produtos.map(produtoMapper::toResponseDTO);
    }

}
