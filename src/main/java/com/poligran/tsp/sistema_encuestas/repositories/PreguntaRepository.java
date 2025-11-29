package com.poligran.tsp.sistema_encuestas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poligran.tsp.sistema_encuestas.entities.Encuesta;
import com.poligran.tsp.sistema_encuestas.entities.Pregunta;

@Repository
public interface PreguntaRepository extends JpaRepository<Pregunta, Long>{
    List<Pregunta> findAllByEncuesta(Encuesta encuesta);
}
