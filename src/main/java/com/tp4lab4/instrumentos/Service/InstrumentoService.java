package com.tp4lab4.instrumentos.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
