package br.ifrn.edu.jeferson.ecommerce.service;

import br.ifrn.edu.jeferson.ecommerce.domain.Categoria;
import br.ifrn.edu.jeferson.ecommerce.domain.Produto;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Categoria.CategoriaRequestDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Categoria.CategoriaResponseDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Produto.ProdutoResponseDTO;
import br.ifrn.edu.jeferson.ecommerce.exception.BusinessException;
import br.ifrn.edu.jeferson.ecommerce.exception.ResourceNotFoundException;
import br.ifrn.edu.jeferson.ecommerce.mapper.CategoriaMapper;
import br.ifrn.edu.jeferson.ecommerce.mapper.ProdutoMapper;
import br.ifrn.edu.jeferson.ecommerce.repository.CategoriaRepository;
import br.ifrn.edu.jeferson.ecommerce.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CategoriaMapper mapper;
    @Autowired
    private CategoriaMapper categoriaMapper;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ProdutoMapper produtoMapper;

    public CategoriaResponseDTO salvar(CategoriaRequestDTO categoriaDto) {
        var categoria =  mapper.toEntity(categoriaDto);

        if (categoriaRepository.existsByNome(categoria.getNome())) {
            throw new BusinessException("Já existe uma categoria com esse nome");
        }

        categoriaRepository.save(categoria);
        return mapper.toResponseDTO(categoria);
    }

    public List<CategoriaResponseDTO> lista(){
        List<Categoria> categorias = categoriaRepository.findAll();
        return mapper.toDTOList (categorias);
    }

    public void deletar(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoria não encontrada");
        }
        categoriaRepository.deleteById(id);
    }

    public CategoriaResponseDTO atualizar(Long id, CategoriaRequestDTO categoriaDto) {
        Categoria categoria = categoriaRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("Categoria não encontrada"));

        if (!categoria.getNome().equals(categoriaDto.getNome()) && categoriaRepository.existsByNome( categoriaDto.getNome()) ) {
            throw  new BusinessException("Já existe uma categoria com esse nome");
        }

        categoriaMapper.updateEntityFromDTO(categoriaDto, categoria);
        var categoriaAlterada = categoriaRepository.save(categoria);

        return categoriaMapper.toResponseDTO(categoriaAlterada);
    }

    public CategoriaResponseDTO buscarPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id).orElseThrow( () -> new ResourceNotFoundException("Categoria não encontrada"));
        return categoriaMapper.toResponseDTO(categoria);
    }

    @Transactional
    public ProdutoResponseDTO bindCategory(Long cat_id, Long pro_id){
        Categoria categoria = categoriaRepository.findById(cat_id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        Produto produto = produtoRepository.findById(pro_id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        categoria.getProdutos().add(produto);
        produto.getCategorias().add(categoria);
        Produto saved_produto = produtoRepository.save(produto);
        categoriaRepository.save(categoria);

        return produtoMapper.toResponseDTO(saved_produto);
    }

    @Transactional
    public ProdutoResponseDTO removeCategoryBind(Long cat_id, Long pro_id){
        Categoria categoria = categoriaRepository.findById(cat_id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        Produto produto = produtoRepository.findById(pro_id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));

        categoria.getProdutos().remove(produto);
        produto.getCategorias().remove(categoria);
        Produto saved_produto = produtoRepository.save(produto);
        categoriaRepository.save(categoria);

        return produtoMapper.toResponseDTO(saved_produto);
    }

}
