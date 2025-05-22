package com.tp4lab4.instrumentos.Repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tp4lab4.instrumentos.Model.PedidoDetalle;

@Repository
public interface PedidoDetalleRepository extends JpaRepository<PedidoDetalle, Long>, PedidoDetalleRepositoryCustom {
    List<PedidoDetalle> findByPedidoId(Long pedidoId);

   @Query("SELECT pd.instrumento.instrumento, SUM(pd.cantidad) " +
           "FROM PedidoDetalle pd " +
           "GROUP BY pd.instrumento.instrumento " +
           "ORDER BY pd.instrumento.instrumento")
    List<Object[]> getCantidadPedidosPorInstrumento();

}
