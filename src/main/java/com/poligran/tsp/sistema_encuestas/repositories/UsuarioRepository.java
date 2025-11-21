package com.poligran.tsp.sistema_encuestas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.poligran.tsp.sistema_encuestas.entities.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long>{
    
}
