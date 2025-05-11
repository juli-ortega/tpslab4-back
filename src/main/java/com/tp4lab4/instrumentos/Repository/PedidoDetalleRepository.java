package com.tp4lab4.instrumentos.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tp4lab4.instrumentos.Model.PedidoDetalle;

@Repository
public interface PedidoDetalleRepository extends JpaRepository<PedidoDetalle, Long> {
    List<PedidoDetalle> findByPedidoId(Long pedidoId);
}
