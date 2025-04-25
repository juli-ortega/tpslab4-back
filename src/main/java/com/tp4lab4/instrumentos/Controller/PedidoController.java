package com.tp4lab4.instrumentos.Controller;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tp4lab4.instrumentos.Model.Pedido;
import com.tp4lab4.instrumentos.Service.PedidoService;

import lombok.AllArgsConstructor;


@RestController
@AllArgsConstructor
@RequestMapping("api/v1/pedido")
public class PedidoController {
    
    private final PedidoService pedidoService;

    @PostMapping("")
    public ResponseEntity<?> createPedido(@RequestBody Pedido pedido) {
        try {
            return ResponseEntity.ok(pedidoService.createPedido(pedido));
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
}


