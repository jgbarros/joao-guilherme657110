package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.controller;

import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto.RegionalRequest;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto.RegionalResponse;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.service.RegionalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

    @PostMapping
    @Operation(summary = "Criar regional", description = "Cria uma nova regional")
    public ResponseEntity<RegionalResponse> create(@Validated @RequestBody RegionalRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar regional", description = "Atualiza os dados de uma regional existente")
    public ResponseEntity<RegionalResponse> update(@PathVariable Long id, @Validated @RequestBody RegionalRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar regional", description = "Remove uma regional pelo seu ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
