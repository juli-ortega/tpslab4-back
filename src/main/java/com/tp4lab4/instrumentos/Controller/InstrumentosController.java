package com.tp4lab4.instrumentos.Controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tp4lab4.instrumentos.Model.Instrumento;
import com.tp4lab4.instrumentos.Model.Dto.InstrumentoDto;
import com.tp4lab4.instrumentos.Service.InstrumentoService;
import com.tp4lab4.instrumentos.Utils.InstrumentosWrapper;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/instrumento")
public class InstrumentosController {

    private final InstrumentoService instrumentoService;

    @GetMapping
    public ResponseEntity<List<Instrumento>> getInstrumentos() {
        return ResponseEntity.ok(instrumentoService.getAllInstrumentos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Instrumento> getInstrumentoById(@PathVariable Long id) {
        return ResponseEntity.ok().body(instrumentoService.getInstrumentoById(id));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createInstrumento(
            @RequestPart("instrumento") String instrumentoString,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            InstrumentoDto instrumentoDto = new ObjectMapper().readValue(instrumentoString, InstrumentoDto.class);

            if (file != null && !file.isEmpty()) {
                instrumentoDto.setImagen(instrumentoService.saveImage(file));
            }

            Instrumento savedInstrumento = instrumentoService.saveInstrumento(
                    instrumentoService.mapDtoToEntity(instrumentoDto));

            return ResponseEntity.status(HttpStatus.CREATED).body(savedInstrumento);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Error al procesar la solicitud", "details", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateInstrumento(
            @PathVariable Long id,
            @RequestPart("instrumento") String instrumentoString,
            @RequestPart(value = "file", required = false) MultipartFile file) {

        try {
            InstrumentoDto instrumentoDto = new ObjectMapper().readValue(instrumentoString, InstrumentoDto.class);

            if (file != null && !file.isEmpty()) {
                instrumentoDto.setImagen(instrumentoService.saveImage(file));
            }

            Instrumento updatedInstrumento = instrumentoService.updateInstrumento(
                    id, instrumentoService.mapDtoToEntity(instrumentoDto));

            return ResponseEntity.ok(updatedInstrumento);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Error al actualizar", "details", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInstrumento(@PathVariable Long id) {
        instrumentoService.deleteInstrumento(id);
        return ResponseEntity.noContent().build();
    }
}