package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.controller;

import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto.ArtistaRequest;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto.ArtistaResponse;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.service.ArtistaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/artistas")
@RequiredArgsConstructor
public class ArtistaController {

    private final ArtistaService service;

    @GetMapping
    public ResponseEntity<Page<ArtistaResponse>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String filtro) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nome"));
        return ResponseEntity.ok(service.findAll(pageable, filtro));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArtistaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<ArtistaResponse> create(@Validated @RequestBody ArtistaRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArtistaResponse> update(@PathVariable Long id, @Validated @RequestBody ArtistaRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
