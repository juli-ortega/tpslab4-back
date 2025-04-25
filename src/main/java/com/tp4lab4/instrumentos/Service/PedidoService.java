package com.tp4lab4.instrumentos.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tp4lab4.instrumentos.Model.Pedido;
import com.tp4lab4.instrumentos.Repository.PedidoRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PedidoService {
    
    @Autowired
    private PedidoRepository pedidoRepository;

    public Pedido createPedido(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> getAllPedidos() {
        return pedidoRepository.findAll();
    }

    public Pedido getPedidoById(Long id) {
        return pedidoRepository.findById(id).orElse(null);
    }
}
