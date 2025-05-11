package com.tp4lab4.instrumentos.Model.Dto;

import com.tp4lab4.instrumentos.Model.Categoria;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InstrumentoDto {
    private Long id;
    
    private String instrumento;

    private String marca;

    private String modelo;

    private String imagen;

    private Double precio;

    private String costoEnvio;

    private Integer cantidadVendida;
    
    private String descripcion;

    private Categoria categoria;

    private int cantidad;
}
