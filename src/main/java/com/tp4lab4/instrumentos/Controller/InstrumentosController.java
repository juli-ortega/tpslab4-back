package com.tp4lab4.instrumentos.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tp4lab4.instrumentos.Model.Instrumento;
import com.tp4lab4.instrumentos.Service.InstrumentoService;
import com.tp4lab4.instrumentos.Utils.InstrumentosWrapper;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/instrumento")
public class InstrumentosController {

    private final InstrumentoService instrumentoService;

    @GetMapping("")
    public ResponseEntity<?> getInstrumentos() {
        return ResponseEntity.ok(instrumentoService.getAllInstrumentos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInstrumentoById(@PathVariable Long id) {
        Instrumento instrumento = instrumentoService.getInstrumentoById(id);
        if (instrumento != null) {
            return ResponseEntity.ok(instrumento);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/save")
    public ResponseEntity<?> createInstrumento(Instrumento instrumento) {
        Instrumento savedInstrumento = instrumentoService.saveInstrumento(instrumento);
        return ResponseEntity.ok(savedInstrumento);
    }

    @PostMapping("/saveAll")
    public ResponseEntity<?> saveAll(@RequestBody InstrumentosWrapper instrumentosWrapper) {
        return ResponseEntity.ok(instrumentoService.saveAll(instrumentosWrapper.getInstrumentos()));
    }

}
