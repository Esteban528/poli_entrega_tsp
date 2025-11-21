package com.poligran.tsp.sistema_encuestas.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.poligran.tsp.sistema_encuestas.entities.Empresa;
import com.poligran.tsp.sistema_encuestas.entities.Usuario;
import com.poligran.tsp.sistema_encuestas.repositories.UsuarioRepository;

@Configuration
public class DataInitializer {
    @Bean
    CommandLineRunner init(UsuarioRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) {
                Usuario usuario = Usuario.builder()
                    .email("admin@poligran.edu.co")
                    .password(passwordEncoder.encode("admin"))
                    .build();
                userRepository.save(usuario);
            }
        };
    }
}
