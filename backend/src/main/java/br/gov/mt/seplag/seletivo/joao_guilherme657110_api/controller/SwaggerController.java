package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class SwaggerController {

    @GetMapping
    public String swaggerUi() {
        return "redirect:/swagger-ui.html";
    }
}
