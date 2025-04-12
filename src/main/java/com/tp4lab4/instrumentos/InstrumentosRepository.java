package com.tp4lab4.instrumentos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstrumentosRepository extends JpaRepository<Instrumento, Long> {

}
