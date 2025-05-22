package com.tp4lab4.instrumentos.Repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tp4lab4.instrumentos.Model.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findAllByOrderByFechaDesc();
    Optional<Pedido> findByPreferenceId(String preferenceId);

    @Query(value = "SELECT mes_anio, cantidad FROM ( " +
               "SELECT TO_CHAR(fecha, 'MM-YYYY') AS mes_anio, COUNT(*) AS cantidad, " +
               "TO_CHAR(fecha, 'YYYY')::int AS anio, TO_CHAR(fecha, 'MM')::int AS mes " +
               "FROM pedido " +
               "GROUP BY mes_anio, anio, mes " +
               ") AS sub " +
               "ORDER BY anio, mes",
       nativeQuery = true)
    List<Object[]> obtenerCantidadPedidosPorMes();

}