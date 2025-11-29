package com.poligran.tsp.sistema_encuestas.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poligran.tsp.sistema_encuestas.dtos.EncuestaDTO;
import com.poligran.tsp.sistema_encuestas.dtos.PreguntaDTO;
import com.poligran.tsp.sistema_encuestas.dtos.RespuestaEncuestaDTO;
import com.poligran.tsp.sistema_encuestas.entities.Empresa;
import com.poligran.tsp.sistema_encuestas.entities.Encuesta;
import com.poligran.tsp.sistema_encuestas.entities.Pregunta;
import com.poligran.tsp.sistema_encuestas.entities.Respuesta;
import com.poligran.tsp.sistema_encuestas.repositories.EncuestaRepository;
import com.poligran.tsp.sistema_encuestas.repositories.PreguntaRepository;
import com.poligran.tsp.sistema_encuestas.repositories.RespuestaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EncuestaService {
    private final EncuestaRepository encuestaRepository;
    private final PreguntaRepository preguntaRepository;
    private final RespuestaRepository respuestaRepository;
    private static final Logger logger = LoggerFactory.getLogger(EncuestaService.class);

    public Encuesta createEncuesta(EncuestaDTO encuestaDTO, Empresa empresa) {
        Encuesta encuesta = Encuesta.builder()
                .empresa(empresa)
                .title(encuestaDTO.getTitle())
                .fechaInicio(LocalDate.now())
                .preguntas(new ArrayList<>())
                .fechaFin(encuestaDTO.getFechaFin())
                .build();

        return saveEncuesta(encuesta);
    }

    public Encuesta saveEncuesta(Encuesta encuesta) {
        return encuestaRepository.save(encuesta);
    }

    public Encuesta saveEncuesta(Encuesta encuestaTmp, Long id) {
        var encuesta = findEncuestaById(id);
        encuesta.setFechaFin(encuestaTmp.getFechaFin());
        encuesta.setTitle(encuestaTmp.getTitle());
        return encuestaRepository.save(encuesta);
    }

    public List<Encuesta> findEncuestasByEmpresa(Empresa empresa) {
        return encuestaRepository.findAllByEmpresa(empresa);
    }

    @Transactional
    public Encuesta findEncuestaById(Long id) throws NoSuchElementException {
        return encuestaRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
    }

    public List<Pregunta> findPreguntasByEncuesta(Encuesta encuesta) {
        return preguntaRepository.findAllByEncuesta(encuesta);
    }

    @Transactional
    public Pregunta addPregunta(PreguntaDTO preguntaDTO) {
        Encuesta encuesta = findEncuestaById(preguntaDTO.getEncuestaId());
        Pregunta pregunta = Pregunta.builder()
                .encuesta(encuesta)
                .title(preguntaDTO.getTitle())
                .type(preguntaDTO.getType())
                .respuestas(List.of())
                .build();

        return preguntaRepository.save(pregunta);
    }

    @Transactional
    public void removePregunta(Long idPregunta) {
        preguntaRepository.deleteById(idPregunta);
    }

    @Transactional
    public void removeEncuesta(Long id) {
        Encuesta encuesta = findEncuestaById(id);

        encuesta.getPreguntas().parallelStream()
                .map(p -> preguntaRepository.findById(p.getId()).orElseThrow())
                .flatMap(p -> p.getRespuestas().stream())
                .map(p-> {
                    logger.info("{}", p);
                    return p;
                })
                .forEach(respuestaRepository::delete);

        encuestaRepository.deleteById(id);
    }

    @Transactional
    public void saveRespuestas(RespuestaEncuestaDTO respuestaEncuestaDTO) {
        respuestaEncuestaDTO.getPreguntas().forEach((k, v) -> {
            Pregunta pregunta = preguntaRepository.findById(k).orElseThrow();
            respuestaRepository.save(Respuesta.builder()
                    .pregunta(pregunta)
                    .value(v)
                    .build());
        });
    }

    @Transactional
    public Map<String, List<String>> findEncuestaRespuestasFormat(Long id) {
        Encuesta encuesta = findEncuestaById(id);
        Map<String, List<String>> map = new TreeMap<>();
        encuesta.getPreguntas().forEach(p -> {
            Pregunta pre = preguntaRepository.findById(p.getId()).orElseThrow();
            List<String> values = new ArrayList<>(pre.getRespuestas().size());
            map.put(pre.getTitle(), values);
            for(Respuesta res: pre.getRespuestas()){
                values.add(res.getValue());
            }
        });
        return map;
    }
}
