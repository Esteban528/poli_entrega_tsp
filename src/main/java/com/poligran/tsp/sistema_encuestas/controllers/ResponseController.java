package com.poligran.tsp.sistema_encuestas.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poligran.tsp.sistema_encuestas.dtos.RespuestaEncuestaDTO;
import com.poligran.tsp.sistema_encuestas.services.EncuestaService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class ResponseController {

    private final EncuestaService encuestaService;
    private static final Logger logger = LoggerFactory.getLogger(ResponseController.class);

    @GetMapping("/r/{id}")
    String fillEncuesta(@PathVariable Long id, Model model) {
        var encuesta = encuestaService.findEncuestaById(id);

        if (LocalDate.now().isAfter(encuesta.getFechaFin()))
            return "redirect:/";

        var respuestaEncuestaDTO = new RespuestaEncuestaDTO();
        respuestaEncuestaDTO.setEncuestaId(encuesta.getId());

        model.addAttribute("encuesta", encuesta);
        model.addAttribute("respuesta", encuesta);
        return "fill_encuesta";
    }

    @PostMapping("/r/{id}")
    String sendEncuesta(RespuestaEncuestaDTO respuesta) {
        encuestaService.saveRespuestas(respuesta);
        return "redirect:/?success";
    }
}
