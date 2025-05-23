package com.tp4lab4.instrumentos.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne
    private Categoria categoria;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean deleted = false;

    public Object map(Object object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'map'");
    }
}
