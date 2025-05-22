package com.tp4lab4.instrumentos.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tp4lab4.instrumentos.Model.Pedido;

import com.tp4lab4.instrumentos.Model.PreferenceMp;
import com.tp4lab4.instrumentos.Model.Dto.PagoUpdateDto;
import com.tp4lab4.instrumentos.Model.Dto.PedidoDto;
import com.tp4lab4.instrumentos.Repository.PedidoRepository;
import com.tp4lab4.instrumentos.Service.PedidoDetalleService;
import com.tp4lab4.instrumentos.Service.PedidoService;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/pedido")
public class PedidoController {

    private final PedidoService pedidoService;
    private final PedidoDetalleService pedidoDetalleService;
    private final PedidoRepository pedidoRepository;

    @PostMapping("")
    public ResponseEntity<?> createPedido(@RequestBody PedidoDto pedidoRequest) {
        try {
            Pedido pedidoSaved = pedidoService.createPedido(pedidoRequest.getPedido());

            Map<String, Object> response = new HashMap<>();
            response.put("Pedido", pedidoSaved);
            response.put("Pedidos detalles",
                    pedidoDetalleService.createPedidoDetalle(pedidoSaved, pedidoRequest.getInstrumentos()));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el pedido", e);
        }
    }

    @PostMapping("/crear-con-mp")
    @Transactional
    public ResponseEntity<?> crearPedidoConPreferencia(@RequestBody PedidoDto pedidoRequest) {
        try {
            // 1. Guardar el pedido en la base de datos
            Pedido pedidoGuardado = pedidoService.createPedido(pedidoRequest.getPedido());

            // 2. Crear los detalles del pedido
            var detalles = pedidoDetalleService.createPedidoDetalle(pedidoGuardado, pedidoRequest.getInstrumentos());

            // 3. Crear la preferencia de MercadoPago con el pedido real (con ID y total)
            MercadoPagoController mercadoPagoController = new MercadoPagoController();
            PreferenceMp preferencia = mercadoPagoController.getPreferenciaIdMercadoPago(pedidoGuardado, detalles);
            pedidoGuardado.setPreferenceId(preferencia.getId());
            pedidoRepository.save(pedidoGuardado);

            // 4. Responder con todo
            Map<String, Object> response = new HashMap<>();
            response.put("pedido", pedidoGuardado);
            response.put("detalles", detalles);
            response.put("preferenciaMp", preferencia);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            throw new RuntimeException("Error al crear el pedido con preferencia MP", e);
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

    @PostMapping("/actualizar-status")
    public ResponseEntity<?> actualizarEstadoPago(@RequestBody PagoUpdateDto pagoUpdate) {
        Optional<Pedido> pedidoOpt = pedidoService.findByPreferenceId(pagoUpdate.getPreferenceId());

        if (pedidoOpt.isPresent()) {
            Pedido pedido = pedidoOpt.get();
            try {
                pedido.setStatusPay(pagoUpdate.getStatus());
                pedidoRepository.save(pedido);
                return ResponseEntity.ok("Estado actualizado");
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Estado inválido: " + pagoUpdate.getStatus());
            }

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido no encontrado");
        }
    }

    /*@PostMapping("/createmp")
    public PreferenceMp createPreferenceMercadoPago(@RequestBody Pedido pedido) {
        MercadoPagoController mercadoPagoController = new MercadoPagoController();
        PreferenceMp preferenceMp = mercadoPagoController.getPreferenciaIdMercadoPago(pedido);
        return preferenceMp;
    }*/

}
