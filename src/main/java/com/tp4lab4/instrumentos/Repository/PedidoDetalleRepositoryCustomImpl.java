package com.tp4lab4.instrumentos.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class PedidoDetalleRepositoryCustomImpl implements PedidoDetalleRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Object[]> getReportePedidosDetallado(LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
        StringBuilder sql = new StringBuilder("""
            SELECT p.fecha, i.instrumento, i.marca, i.modelo, pd.cantidad, i.precio, (pd.cantidad * i.precio) AS subtotal
            FROM pedido_detalle pd
            JOIN pedido p ON pd.pedido_id = p.id
            JOIN instrumentos i ON pd.instrumento_id = i.id
            WHERE 1=1
        """);

        if (fechaDesde != null) {
            sql.append(" AND p.fecha >= :fechaDesde");
        }
        if (fechaHasta != null) {
            sql.append(" AND p.fecha <= :fechaHasta");
        }

        sql.append(" ORDER BY p.fecha");

        Query query = entityManager.createNativeQuery(sql.toString());

        if (fechaDesde != null) {
            query.setParameter("fechaDesde", fechaDesde);
        }
        if (fechaHasta != null) {
            query.setParameter("fechaHasta", fechaHasta);
        }

        return query.getResultList();
    }
}
