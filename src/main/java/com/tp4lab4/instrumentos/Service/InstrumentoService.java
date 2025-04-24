package com.tp4lab4.instrumentos.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.tp4lab4.instrumentos.Model.Instrumento;
import com.tp4lab4.instrumentos.Repository.InstrumentosRepository;

@Service
public class InstrumentoService {
    
    @Autowired
    private InstrumentosRepository instrumentosRepository;
    
    public List<Instrumento> getAllInstrumentos() {
        return instrumentosRepository.findAll();
    }

    public Instrumento getInstrumentoById(Long id) {
        return instrumentosRepository.findById(id).orElse(null);
    }

    public Instrumento saveInstrumento(Instrumento instrumento) {
        return instrumentosRepository.save(instrumento);
    }

    public void deleteInstrumento(Long id) {
        instrumentosRepository.deleteById(id);
    }

    public List<Instrumento> saveAll(List<Instrumento> instrumentos) {
        return instrumentosRepository.saveAll(instrumentos);
    }

    public Instrumento updateInstrumento(Long id, Instrumento instrumento) {
        Instrumento existingInstrumento = getInstrumentoById(id);

        existingInstrumento.setInstrumento(instrumento.getInstrumento());
        existingInstrumento.setMarca(instrumento.getMarca());
        existingInstrumento.setModelo(instrumento.getModelo());
        existingInstrumento.setImagen(instrumento.getImagen());
        existingInstrumento.setPrecio(instrumento.getPrecio());
        existingInstrumento.setCostoEnvio(instrumento.getCostoEnvio());
        existingInstrumento.setCantidadVendida(instrumento.getCantidadVendida());
        existingInstrumento.setDescripcion(instrumento.getDescripcion());
        existingInstrumento.setCategoria(instrumento.getCategoria());

        return instrumentosRepository.save(existingInstrumento);
    }
    
    public String saveImage(MultipartFile file) {
         try {
            // Guardar imagen
            String uploadDir = "uploads/";
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String filePath = uploadDir + file.getOriginalFilename();
            file.transferTo(new File(filePath));

            return file.getOriginalFilename();
        } catch (IOException e) {
            return "Error al guardar imagen: " + e.getMessage();
        }
    }

}
