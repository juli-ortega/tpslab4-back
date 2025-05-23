package com.tp4lab4.instrumentos.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tp4lab4.instrumentos.Model.Instrumento;

@Repository
public interface InstrumentosRepository extends JpaRepository<Instrumento, Long> {
    
    @Query("SELECT i FROM Instrumento i WHERE i.deleted = false")
    List<Instrumento> findAllActivos();

    List<Instrumento> findByDeletedFalse();

}
