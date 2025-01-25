package br.ifrn.edu.jeferson.ecommerce.service;

import br.ifrn.edu.jeferson.ecommerce.domain.Cliente;
import br.ifrn.edu.jeferson.ecommerce.domain.Endereco;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Categoria.CategoriaRequestDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Endereco.EnderecoRequestDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Endereco.EnderecoResponseDTO;
import br.ifrn.edu.jeferson.ecommerce.exception.BusinessException;
import br.ifrn.edu.jeferson.ecommerce.exception.ResourceNotFoundException;
import br.ifrn.edu.jeferson.ecommerce.mapper.EnderecoMapper;
import br.ifrn.edu.jeferson.ecommerce.repository.ClienteRepository;
import br.ifrn.edu.jeferson.ecommerce.repository.EnderecoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EnderecoService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private EnderecoMapper enderecoMapper;

    public EnderecoResponseDTO salvar(EnderecoRequestDTO enderecoDTO) {
        var endereco = enderecoMapper.toEntity(enderecoDTO);

        Cliente client = clienteRepository.findById(enderecoDTO.getClienteId())
                        .orElseThrow(() -> new BusinessException("Cliente com ID especificado não existe"));

        if(client.getEndereco() != null){
            throw new BusinessException("Cliente já possui endereço");
        }

        endereco.setCliente(client);

        clienteRepository.save(client);
        enderecoRepository.save(endereco);

        return enderecoMapper.toResponseDTO(endereco);
    }

    @Cacheable(value = "enderecos")
    public List<EnderecoResponseDTO> listar() {
        List<Endereco> enderecos = enderecoRepository.findAll();
        return enderecoMapper.toEnderecosDTOList(enderecos);
    }

    public EnderecoResponseDTO update(Long id, EnderecoRequestDTO enderecoDTO) {
        var endereco = enderecoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(
                "Endereço não encontrado"
        ));

        enderecoMapper.updateEntityFromDTO(enderecoDTO, endereco);
        var updated_endereco = enderecoRepository.save(endereco);

        return enderecoMapper.toResponseDTO(updated_endereco);
    }

    public void remover(Long id) {
        if(!enderecoRepository.existsById(id)){
            throw new ResourceNotFoundException("Endereço não encontrado");
        }
        enderecoRepository.deleteById(id);
    }

    public EnderecoResponseDTO buscarPorId(Long id) {
        var endereco = enderecoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Endereco Não Encontrado"));

        return enderecoMapper.toResponseDTO(endereco);
    }
}
