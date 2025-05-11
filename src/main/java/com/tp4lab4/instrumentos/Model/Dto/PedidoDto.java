package com.tp4lab4.instrumentos.Model.Dto;

import java.util.List;

import com.tp4lab4.instrumentos.Model.Pedido;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PedidoDto {
    private Pedido pedido;
    private List<InstrumentoDto> instrumentos; 
}
