package com.poligran.tsp.sistema_encuestas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poligran.tsp.sistema_encuestas.entities.Empresa;
import com.poligran.tsp.sistema_encuestas.entities.Encuesta;

@Repository
public interface EncuestaRepository extends JpaRepository<Encuesta, Long> {
    List<Encuesta> findAllByEmpresa(Empresa empresa);
}
