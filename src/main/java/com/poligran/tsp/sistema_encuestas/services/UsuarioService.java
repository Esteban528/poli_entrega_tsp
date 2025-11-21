package com.poligran.tsp.sistema_encuestas.services;

import org.springframework.stereotype.Service;

import com.poligran.tsp.sistema_encuestas.entities.Usuario;
import com.poligran.tsp.sistema_encuestas.repositories.UsuarioRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

}
