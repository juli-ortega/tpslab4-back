package com.tp4lab4.instrumentos.Utils;

import java.util.List;

import com.tp4lab4.instrumentos.Model.Instrumento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstrumentosWrapper {
    private List<Instrumento> instrumentos;
}
