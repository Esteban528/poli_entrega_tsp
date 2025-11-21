package com.poligran.tsp.sistema_encuestas.services;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.poligran.tsp.sistema_encuestas.entities.Usuario;
import com.poligran.tsp.sistema_encuestas.repositories.UsuarioRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    Usuario findUsuarioByEmail(String email) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Not Found"));
    }

}
