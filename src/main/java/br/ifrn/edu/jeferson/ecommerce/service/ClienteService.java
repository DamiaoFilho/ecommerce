package br.ifrn.edu.jeferson.ecommerce.service;

import br.ifrn.edu.jeferson.ecommerce.domain.Cliente;
import br.ifrn.edu.jeferson.ecommerce.domain.Endereco;
import br.ifrn.edu.jeferson.ecommerce.domain.Pedido;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Cliente.ClienteRequestDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Cliente.ClienteResponseDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Endereco.EnderecoRequestDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.ItemPedido.ItemPedidoResponseDTO;
import br.ifrn.edu.jeferson.ecommerce.domain.dtos.Pedido.PedidoResponseDTO;
import br.ifrn.edu.jeferson.ecommerce.exception.BusinessException;
import br.ifrn.edu.jeferson.ecommerce.exception.ResourceNotFoundException;
import br.ifrn.edu.jeferson.ecommerce.mapper.ClienteMapper;
import br.ifrn.edu.jeferson.ecommerce.mapper.EnderecoMapper;
import br.ifrn.edu.jeferson.ecommerce.mapper.PedidoMapper;
import br.ifrn.edu.jeferson.ecommerce.repository.ClienteRepository;
import br.ifrn.edu.jeferson.ecommerce.repository.EnderecoRepository;
import br.ifrn.edu.jeferson.ecommerce.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private EnderecoMapper enderecoMapper;

    @Autowired
    private ClienteMapper clienteMapper;

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private PedidoMapper pedidoMapper;

    @Transactional
    public ClienteResponseDTO cadastrarCliente(ClienteRequestDTO clienteRequestDTO) {
        Cliente cliente = clienteMapper.toEntity(clienteRequestDTO);

        if(clienteRepository.existsByEmail(cliente.getEmail())){
            throw new BusinessException("Email já existente");
        }
        if(clienteRepository.existsByCpf(cliente.getCpf())){
            throw new BusinessException("CPF já existente");
        }
        if(clienteRepository.existsByTelefone(cliente.getTelefone())){
            throw new BusinessException("Telefone ja existente");
        }

        Cliente new_cliente = clienteRepository.save(cliente);

        Endereco endereco = new Endereco();
        endereco.setCliente(new_cliente);
        endereco.setCep(clienteRequestDTO.getEndereco().getCep());
        endereco.setEstado(clienteRequestDTO.getEndereco().getEstado());
        endereco.setCidade(clienteRequestDTO.getEndereco().getCidade());
        endereco.setBairro(clienteRequestDTO.getEndereco().getBairro());
        endereco.setRua(clienteRequestDTO.getEndereco().getRua());
        endereco.setNumero(clienteRequestDTO.getEndereco().getNumero());

        Endereco new_endereco = enderecoRepository.save(endereco);

        new_cliente.setEndereco(new_endereco);
        new_cliente = clienteRepository.save(new_cliente);

        return clienteMapper.toResponseDTO(new_cliente);
    }

    public Page<ClienteResponseDTO> listarClientes(Pageable pageable) {
        Page<Cliente> clientes = clienteRepository.findAll(pageable);
        return clientes.map(clienteMapper::toResponseDTO);
    }

    public ClienteResponseDTO buscarClientePorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        return clienteMapper.toResponseDTO(cliente);
    }

    public ClienteResponseDTO atualizarCliente(Long id, ClienteRequestDTO clienteRequestDTO) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        Endereco endereco = enderecoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Endereco do cliente não encontrado"));

        if(clienteRepository.existsByEmail(clienteRequestDTO.getEmail()) && !cliente.getEmail().equals(clienteRequestDTO.getEmail())){
            throw new BusinessException("Email já existente");
        }
        if(clienteRepository.existsByCpf(clienteRequestDTO.getCpf()) && !cliente.getCpf().equals(clienteRequestDTO.getCpf())){
            throw new BusinessException("CPF já existente");
        }
        if(clienteRepository.existsByTelefone(clienteRequestDTO.getTelefone())  && !cliente.getTelefone().equals(clienteRequestDTO.getTelefone())){
            throw new BusinessException("Telefone ja existente");
        }

        endereco.setCep(clienteRequestDTO.getEndereco().getCep());
        endereco.setEstado(endereco.getEstado());
        endereco.setCidade(endereco.getCidade());
        endereco.setBairro(endereco.getBairro());
        endereco.setRua(endereco.getRua());
        endereco.setNumero(endereco.getNumero());
        enderecoRepository.save(endereco);

        clienteMapper.updateEntityFromDTO(clienteRequestDTO, cliente);
        Cliente updated_cliente = clienteRepository.save(cliente);

        return clienteMapper.toResponseDTO(updated_cliente);
    }

    public void deletarCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        if(cliente.getPedidos() != null || !cliente.getPedidos().isEmpty()){
            throw new BusinessException("Cliente Possui Pedidos");
        }

        enderecoRepository.delete(cliente.getEndereco());
        clienteRepository.delete(cliente);
    }

    public Page<PedidoResponseDTO> listarPedidos(Long id, Pageable pageable) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));
        Page<Pedido> pedidos = pedidoRepository.findByClienteId(id, pageable);
        return pedidos.map(pedidoMapper::toResponseDTO);
    }
}
