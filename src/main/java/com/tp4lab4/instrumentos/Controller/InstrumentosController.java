package com.tp4lab4.instrumentos.Controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tp4lab4.instrumentos.Model.Categoria;
import com.tp4lab4.instrumentos.Model.Instrumento;
import com.tp4lab4.instrumentos.Model.Dto.InstrumentoDto;
import com.tp4lab4.instrumentos.Service.CategoriaService;
import com.tp4lab4.instrumentos.Service.InstrumentoService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/instrumento")
public class InstrumentosController {

    private final InstrumentoService instrumentoService;
    private final CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<Instrumento>> getInstrumentos() {
        return ResponseEntity.ok(instrumentoService.getAllInstrumentos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInstrumentoById(@PathVariable Long id) {
        Instrumento findInstrumento = instrumentoService.getInstrumentoById(id);
        if (findInstrumento == null) {
            return ResponseEntity.badRequest().body("Not found");
        }
        return ResponseEntity.ok().body(findInstrumento);
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
    
            // Asegúrate de que la categoría esté correctamente asignada
            if (instrumentoDto.getCategoria() != null && instrumentoDto.getCategoria().getId() != null) {
                Categoria categoria = categoriaService.getCategoriaById(instrumentoDto.getCategoria().getId());
                instrumentoDto.setCategoria(categoria);
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
            @RequestPart("nombre") String nombre,
            @RequestPart("marca") String marca,
            @RequestPart("modelo") String modelo,
            @RequestPart("precio") String precioStr,
            @RequestPart("costoEnvio") String costoEnvioStr,
            @RequestPart("cantidadVendida") String cantidadVendidaStr,
            @RequestPart("descripcion") String descripcion,
            @RequestPart("categoria") String categoria,
            @RequestPart(value = "imagen", required = false) MultipartFile file) {

        try {
            // Convertir los strings a los tipos adecuados
            Double precio = precioStr != null && !precioStr.isEmpty() ? Double.parseDouble(precioStr) : null;
            String costoEnvio = costoEnvioStr != null && !costoEnvioStr.isEmpty() ? costoEnvioStr : null;
            Integer cantidadVendida = cantidadVendidaStr != null && !cantidadVendidaStr.isEmpty()
                    ? Integer.parseInt(cantidadVendidaStr)
                    : null;
            Long categoriaId = Long.parseLong(categoria);
            Categoria categoriaEntity = categoriaService.getCategoriaById(categoriaId);


            // Crear el DTO manualmente
            InstrumentoDto instrumentoDto = new InstrumentoDto();
            instrumentoDto.setInstrumento(nombre);
            instrumentoDto.setMarca(marca);
            instrumentoDto.setModelo(modelo);
            instrumentoDto.setPrecio(precio);
            instrumentoDto.setCategoria(categoriaEntity);

            // Manejar el caso especial para costoEnvio
            if ("GRATIS".equalsIgnoreCase(costoEnvio)) {
                instrumentoDto.setCostoEnvio("G");
            } else {
                instrumentoDto.setCostoEnvio(String.valueOf(Double.parseDouble(costoEnvio)));
            }

            instrumentoDto.setCantidadVendida(cantidadVendida);
            instrumentoDto.setDescripcion(descripcion);

            // Obtener el instrumento actual de la base de datos
            Instrumento existingInstrumento = instrumentoService.getInstrumentoById(id);

            // Si hay una nueva imagen, actualizarla
            if (file != null && !file.isEmpty()) {
                instrumentoDto.setImagen(instrumentoService.saveImage(file));
            } else {
                // Mantener la imagen actual si no se proporciona una nueva
                instrumentoDto.setImagen(existingInstrumento.getImagen());
            }

            Instrumento updatedInstrumento = instrumentoService.updateInstrumento(
                    id, instrumentoService.mapDtoToEntity(instrumentoDto));

            return ResponseEntity.ok(updatedInstrumento);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Error en el formato de los números", "details", e.getMessage()));
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