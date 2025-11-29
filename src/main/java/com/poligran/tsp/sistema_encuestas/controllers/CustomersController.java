package com.poligran.tsp.sistema_encuestas.controllers;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.poligran.tsp.sistema_encuestas.dtos.EncuestaDTO;
import com.poligran.tsp.sistema_encuestas.dtos.PreguntaDTO;
import com.poligran.tsp.sistema_encuestas.entities.Empresa;
import com.poligran.tsp.sistema_encuestas.entities.Encuesta;
import com.poligran.tsp.sistema_encuestas.entities.Pregunta;
import com.poligran.tsp.sistema_encuestas.services.EncuestaService;
import com.poligran.tsp.sistema_encuestas.services.UsuarioService;
import com.poligran.tsp.sistema_encuestas.utils.CsvComponent;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequestMapping("/customer")
@Controller
@RequiredArgsConstructor
public class CustomersController {
    private final EncuestaService encuestaService;
    private final UsuarioService usuarioService;
    private final CsvComponent csvComponent;
    private static final Logger logger = LoggerFactory.getLogger(CustomersController.class);
    final List<String> preguntaTypes = List.of(
            "text", "email", "date", "1-5", "number", "phone");

    @GetMapping("")
    String showDashboard() {
        return "dashboard";
    }

    @GetMapping("/encuesta")
    String showEncuesta(Model model) {
        var encuestas = encuestaService.findEncuestasByEmpresa(
                usuarioService.getAuthUsuario().getEmpresa());

        model.addAttribute("encuestas", encuestas);
        return "encuesta";
    }

    @GetMapping("/encuesta/create")
    String showCreateEncuesta(Model model) {
        model.addAttribute("encuesta", new EncuestaDTO());

        return "encuesta_create";
    }

    @PostMapping("/encuesta/create")
    String createEncuesta(EncuestaDTO encuesta) {
        encuestaService.createEncuesta(encuesta, usuarioService.getAuthUsuario().getEmpresa());

        return "redirect:/customer/encuesta";
    }

    @GetMapping("/encuesta/edit/{id}")
    String showEditEncuesta(Model model, @PathVariable Long id) {
        Encuesta encuesta = encuestaService.findEncuestaById(id);

        model.addAttribute("encuesta", encuesta);
        var pDto = new PreguntaDTO();
        pDto.setEncuestaId(encuesta.getId());
        model.addAttribute("newPregunta", pDto);
        model.addAttribute("preguntaTypes", preguntaTypes);

        return "encuesta_edit";
    }

    @PostMapping("/encuesta/remove/{id}")
    String removeEncuesta(@PathVariable Long id) {
        encuestaService.removeEncuesta(id);
        return "redirect:/customer/encuesta";
    }

    @PostMapping("/encuesta/edit/{id}")
    String editEncuesta(Encuesta encuesta, @PathVariable Long id) {
        encuestaService.saveEncuesta(encuesta, id);
        return "redirect:/customer/encuesta/edit/{id}";
    }

    @PostMapping("/encuesta/pregunta/add/{id}")
    String addPregunta(PreguntaDTO preguntaDTO) {
        encuestaService.addPregunta(preguntaDTO);
        return "redirect:/customer/encuesta/edit/{id}";
    }

    @PostMapping("/encuesta/pregunta/remove/{id}")
    String deleteRemove(@RequestParam(name = "id") Long id) {
        encuestaService.removePregunta(id);
        return "redirect:/customer/encuesta/edit/{id}";
    }

    @GetMapping("/report/{id}")
    void showReports(@PathVariable Long id, HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"data.csv\"");
        var content = encuestaService.findEncuestaRespuestasFormat(id);
        csvComponent.writeStudentsToCsv(response.getWriter(), content);
    }
}
