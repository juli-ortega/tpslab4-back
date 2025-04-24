package com.tp4lab4.instrumentos.Controller;

import java.io.File;
import java.io.IOException;

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

    @PostMapping(value = "/save", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createInstrumento(@RequestPart("instrumento") String instrumentoString, @RequestPart("file") MultipartFile file) throws IOException {
        try {
            System.out.println(instrumentoString);
            ObjectMapper objectMapper = new ObjectMapper();
            InstrumentoDto instrumentoDto = objectMapper.readValue(instrumentoString, InstrumentoDto.class);
            System.out.println(instrumentoDto);
    
            // Guardar imagen y setear en el DTO
            instrumentoDto.setImagen(instrumentoService.saveImage(file));
    
            // Mapear DTO a entidad
            Instrumento instrumento = instrumentoService.mapDtoToEntity(instrumentoDto);
    
            // Guardar entidad
            Instrumento savedInstrumento = instrumentoService.saveInstrumento(instrumento);
    
            return ResponseEntity.ok(savedInstrumento);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el instrumentos: " + e.getMessage());
        }
    }
    


    @PostMapping("/saveAll")
    public ResponseEntity<?> saveAll(@RequestBody InstrumentosWrapper instrumentosWrapper) {
        return ResponseEntity.ok(instrumentoService.saveAll(instrumentosWrapper.getInstrumentos()));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateInstrumento(@PathVariable Long id ,@RequestBody Instrumento instrumento) {
        Instrumento updatedInstrumento = instrumentoService.updateInstrumento(id, instrumento);
        return ResponseEntity.ok(updatedInstrumento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInstrumento(@PathVariable Long id) {
        instrumentoService.deleteInstrumento(id);
        return ResponseEntity.ok("Instrumento deleted successfully");
    }

}
