package com.poligran.tsp.sistema_encuestas.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poligran.tsp.sistema_encuestas.entities.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, Long>{

    Optional<Empresa> findByNombre(String nombre);
}
