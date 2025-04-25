package com.tp4lab4.instrumentos.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tp4lab4.instrumentos.Model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

}