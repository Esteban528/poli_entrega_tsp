package com.poligran.tsp.sistema_encuestas.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.poligran.tsp.sistema_encuestas.dtos.UsuarioDTO;
import com.poligran.tsp.sistema_encuestas.entities.Empresa;
import com.poligran.tsp.sistema_encuestas.entities.Usuario;
import com.poligran.tsp.sistema_encuestas.repositories.UsuarioRepository;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Service
@AllArgsConstructor
@Getter
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final EmpresaService empresaService;
    private final PasswordEncoder passwordEncoder;

    public Usuario findUsuarioByEmail(String email) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found"));
    }

    public Usuario getAuthUsuario() {
        SecurityContext sContext = SecurityContextHolder.getContext();
        return findUsuarioByEmail(
                sContext.getAuthentication().getName());
    }

    public List<Usuario> listAll() {
        return usuarioRepository.findAll();
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario save(UsuarioDTO usuarioDTO) {
        Empresa empresa = empresaService.findByNombre(
                usuarioDTO.getNameEmpresa());

        Usuario usuario = Usuario.builder()
                .email(usuarioDTO.getEmail())
                .password(passwordEncoder.encode(usuarioDTO.getPassword()))
                .empresa(empresa)
                .scopes(List.of(new SimpleGrantedAuthority("ROLE_USER")))
                .build();

        return save(usuario);
    }

    public Usuario findById(Long id) throws NoSuchElementException {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException());
    }

    public void delete(Usuario usuario) {
        deleteById(usuario.getId());
    }

    public void deleteById(Long id) {
        usuarioRepository.deleteById(id);
    }
}
