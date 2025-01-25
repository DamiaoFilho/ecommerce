package br.ifrn.edu.jeferson.ecommerce.repository;

import br.ifrn.edu.jeferson.ecommerce.domain.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long>, JpaSpecificationExecutor<Pedido> {
    Page<Pedido> findByClienteId(Long id, Pageable pageable);
}
