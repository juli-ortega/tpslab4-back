package com.tp4lab4.instrumentos.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tp4lab4.instrumentos.Model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

}
