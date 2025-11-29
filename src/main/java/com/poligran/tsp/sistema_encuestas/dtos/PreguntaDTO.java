package com.poligran.tsp.sistema_encuestas.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PreguntaDTO {

    private Long id;

    private String title;

    private String type;

    private Long encuestaId;

}
