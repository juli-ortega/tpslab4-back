package com.tp4lab4.instrumentos;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "instrumentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Instrumento {

    @Id
    private Long id;

    private String instrumento;

    private String marca;

    private String modelo;

    private String imagen;

    private Double precio;

    @Column(name = "costo_envio")
    private String costoEnvio;

    @Column(name = "cantidad_vendida")
    private Integer cantidadVendida;

    @Column(length = 1000)
    private String descripcion;
}
