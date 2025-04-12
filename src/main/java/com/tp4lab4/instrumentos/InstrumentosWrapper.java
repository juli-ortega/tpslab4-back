package com.tp4lab4.instrumentos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstrumentosWrapper {
    private List<Instrumento> instrumentos;
}
