package com.tp4lab4.instrumentos.Service;

import java.util.ArrayList;
import java.util.List;

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

}
