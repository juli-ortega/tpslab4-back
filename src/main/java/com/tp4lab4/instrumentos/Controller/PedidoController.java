package com.tp4lab4.instrumentos.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tp4lab4.instrumentos.Model.Pedido;
import com.tp4lab4.instrumentos.Model.PedidoDetalle;
import com.tp4lab4.instrumentos.Model.Dto.InstrumentoDto;
import com.tp4lab4.instrumentos.Model.Dto.PedidoDto;
import com.tp4lab4.instrumentos.Service.PedidoDetalleService;
import com.tp4lab4.instrumentos.Service.PedidoService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@AllArgsConstructor
@RequestMapping("api/v1/pedido")
public class PedidoController {
    
    private final PedidoService pedidoService;
    private final PedidoDetalleService pedidoDetalleService;

    @PostMapping("")
    public ResponseEntity<?> createPedido(@RequestBody PedidoDto pedidoRequest) {
        try {
            Pedido pedidoSaved = pedidoService.createPedido(pedidoRequest.getPedido());

            Map<String, Object> response = new HashMap<>();
            response.put("Pedido", pedidoSaved);
            response.put("Pedidos detalles", pedidoDetalleService.createPedidoDetalle(pedidoSaved, pedidoRequest.getInstrumentos()));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el pedido", e);
        }
    }


    @GetMapping("")
    public ResponseEntity<?> getAllPedidos() {
        try {
            return ResponseEntity.ok(pedidoService.getAllPedidos());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los pedidos", e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPedidoById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(pedidoService.getPedidoById(id));
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener el pedido", e);
        }
    }

    @GetMapping("/detalle/{id}")
    public ResponseEntity<?> getDetalles(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(pedidoDetalleService.getPedidoDetalles(id));
        } catch (Exception e) {
            throw new RuntimeException("Error en los detalles del pedido", e);
        }
    }
    
}


