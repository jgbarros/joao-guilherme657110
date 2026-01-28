package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.controller;

import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto.RegionalResponse;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.service.RegionalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/regionais")
@RequiredArgsConstructor
@Tag(name = "Regionais", description = "Gerenciamento de Regionais")
public class RegionalController {

    private final RegionalService service;

    @GetMapping
    @Operation(summary = "Listar regionais", description = "Retorna uma lista paginada de regionais")
    public ResponseEntity<Page<RegionalResponse>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String filtro) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nome"));
        return ResponseEntity.ok(service.findAll(pageable, filtro));
    }

    @GetMapping("/ativas")
    @Operation(summary = "Listar regionais ativas", description = "Retorna uma lista de todas as regionais ativas")
    public ResponseEntity<List<RegionalResponse>> listAtivas() {
        return ResponseEntity.ok(service.findAllAtivas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar regional por ID", description = "Retorna uma regional específica pelo seu ID")
    public ResponseEntity<RegionalResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/count")
    @Operation(summary = "Contar regionais", description = "Retorna a quantidade total de regionais cadastradas")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(service.count());
    }
}
