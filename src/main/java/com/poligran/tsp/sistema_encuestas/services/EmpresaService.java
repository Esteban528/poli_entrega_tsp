package com.poligran.tsp.sistema_encuestas.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poligran.tsp.sistema_encuestas.dtos.EmpresaDTO;
import com.poligran.tsp.sistema_encuestas.entities.Empresa;
import com.poligran.tsp.sistema_encuestas.repositories.EmpresaRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmpresaService {

    private final EmpresaRepository empresaRepository;

    public List<Empresa> listAll() {
        return empresaRepository.findAll();
    }

    public Empresa findByNombre(String nombre) throws NoSuchElementException {
        return empresaRepository.findByNombre(nombre)
                .orElseThrow(() -> new NoSuchElementException());
    }

    public Empresa findById(Long id) throws NoSuchElementException {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException());
    }

    public Empresa save(Empresa empresa) {
        return empresaRepository.save(empresa);
    }

    public Empresa save(EmpresaDTO empresaDTO) {
        Empresa empresa = Empresa.builder()
                .nombre(empresaDTO.getNombre())
                .nit(empresaDTO.getNit())
                .direccion(empresaDTO.getDireccion())
                .build();

        return save(empresa);
    }

    public void delete(Empresa empresa) {
        deleteById(empresa.getId());
    }

    public void deleteById(Long id) {
        empresaRepository.deleteById(id);
    }
}
