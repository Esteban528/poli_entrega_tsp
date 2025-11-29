package com.poligran.tsp.sistema_encuestas.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        return "home";
    }
}
