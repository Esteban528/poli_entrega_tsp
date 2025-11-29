package com.poligran.tsp.sistema_encuestas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poligran.tsp.sistema_encuestas.entities.Respuesta;

@Repository
    public interface RespuestaRepository extends JpaRepository<Respuesta, Long> {

}
