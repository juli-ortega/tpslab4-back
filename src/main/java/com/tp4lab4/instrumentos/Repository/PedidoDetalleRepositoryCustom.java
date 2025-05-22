package com.tp4lab4.instrumentos.Repository;

import java.time.LocalDateTime;
import java.util.List;

public interface PedidoDetalleRepositoryCustom {
    List<Object[]> getReportePedidosDetallado(LocalDateTime fechaDesde, LocalDateTime fechaHasta);
}