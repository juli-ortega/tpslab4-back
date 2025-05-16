package com.tp4lab4.instrumentos.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tp4lab4.instrumentos.Model.Categoria;
import com.tp4lab4.instrumentos.Service.CategoriaService;
import com.tp4lab4.instrumentos.Service.InstrumentoService;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1/categoria")
@AllArgsConstructor
public class CategoriaController {

    private CategoriaService categoriaService;
    private InstrumentoService instrumentoService;
    
    @GetMapping("")
    public ResponseEntity<?> getCategorias() {
        return ResponseEntity.ok(categoriaService.getAllCategorias());
    }
    
    @PostMapping("/save")
    public ResponseEntity<?> createCategoria(@RequestBody Categoria categoria) {
        Categoria savedCategoria = categoriaService.saveCategoria(categoria);
        return ResponseEntity.ok(savedCategoria);
    }

    @GetMapping("/filtrar/{categoria}")
    public ResponseEntity<?> getInstrumentosByCategoria(@PathVariable String categoria) {
        return ResponseEntity.ok(instrumentoService.getAllInstrumentos().stream()
                .filter(i -> i.getCategoria().getDenominacion().equalsIgnoreCase(categoria))
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoriaById(@RequestParam Long id) {
        return ResponseEntity.ok(categoriaService.getCategoriaById(id));
    }
    
}
