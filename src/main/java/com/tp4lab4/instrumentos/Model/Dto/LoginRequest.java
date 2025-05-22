package com.tp4lab4.instrumentos.Model.Dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String nombreUsuario;
    private String clave;
}