package com.tp4lab4.instrumentos.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tp4lab4.instrumentos.Model.Categoria;
import com.tp4lab4.instrumentos.Repository.CategoriaRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CategoriaService {
     
    private CategoriaRepository categoriaRepository;

    public List<Categoria> getAllCategorias() {
        return categoriaRepository.findAll();
    }

    public Categoria getCategoriaById(Long id) {
        return categoriaRepository.findById(id).orElse(null);
    }

    public Categoria saveCategoria(Categoria categoria) {
        String denominacion = categoria.getDenominacion().toLowerCase();
        System.out.println(denominacion);
        categoria.setDenominacion(denominacion);
        return categoriaRepository.save(categoria);
    }

}
