package com.tp4lab4.instrumentos.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.tp4lab4.instrumentos.Model.Instrumento;
import com.tp4lab4.instrumentos.Model.Pedido;
import com.tp4lab4.instrumentos.Model.PedidoDetalle;
import com.tp4lab4.instrumentos.Model.Dto.InstrumentoDto;
import com.tp4lab4.instrumentos.Repository.PedidoDetalleRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PedidoDetalleService {
    
    private PedidoDetalleRepository pedidoDetalleRepository;
    private InstrumentoService instrumentoService;

    public List<PedidoDetalle> createPedidoDetalle(Pedido pedido, List<InstrumentoDto> instrumentosDto) {
        List<PedidoDetalle> pedidosDetalles = new ArrayList<>();

        for (InstrumentoDto instrumentoDto : instrumentosDto) {
            PedidoDetalle pedidoDetalle = new PedidoDetalle();
            pedidoDetalle.setCantidad(instrumentoDto.getCantidad());

            Instrumento instrumentoEntity = instrumentoService.getInstrumentoById(instrumentoDto.getId());

            pedidoDetalle.setInstrumento(instrumentoEntity);
            pedidoDetalle.setPedido(pedido);

            pedidosDetalles.add(pedidoDetalle);

            pedidoDetalleRepository.save(pedidoDetalle);
        }


        return pedidosDetalles;
    }

    public List<PedidoDetalle> getPedidoDetalles(Long id){
        return pedidoDetalleRepository.findByPedidoId(id);
    }

    public List<Object[]> getPedidosAgrupadosPorInstrumento() {
        return pedidoDetalleRepository.getCantidadPedidosPorInstrumento();
    }

    public ByteArrayInputStream generarReportePedidos(LocalDateTime fechaDesde, LocalDateTime fechaHasta) throws IOException {

        System.out.println(fechaDesde + "   " + fechaHasta);
        List<Object[]> datos = pedidoDetalleRepository.getReportePedidosDetallado(fechaDesde, fechaHasta);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Pedidos Detallados");

            Row header = sheet.createRow(0);
            String[] columnas = {"Fecha Pedido", "Instrumento", "Marca", "Modelo", "Cantidad", "Precio", "Subtotal"};

            for (int i = 0; i < columnas.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columnas[i]);
            }

            int fila = 1;
            for (Object[] filaDatos : datos) {
                Row row = sheet.createRow(fila++);

                row.createCell(0).setCellValue(filaDatos[0].toString());
                row.createCell(1).setCellValue(filaDatos[1].toString());
                row.createCell(2).setCellValue(filaDatos[2].toString());
                row.createCell(3).setCellValue(filaDatos[3].toString());
                row.createCell(4).setCellValue(Integer.parseInt(filaDatos[4].toString()));
                row.createCell(5).setCellValue(Double.parseDouble(filaDatos[5].toString()));
                row.createCell(6).setCellValue(Double.parseDouble(filaDatos[6].toString()));
            }

            for (int i = 0; i < columnas.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }

}
