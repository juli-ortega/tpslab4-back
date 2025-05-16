package com.tp4lab4.instrumentos.Model.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PagoUpdateDto {
    private String preferenceId;
    private String status;
}