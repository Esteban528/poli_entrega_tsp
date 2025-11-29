package com.poligran.tsp.sistema_encuestas.dtos;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class RespuestaEncuestaDTO {

    private Long encuestaId;

    private Map<Long, String> preguntas = new HashMap<>();
}
