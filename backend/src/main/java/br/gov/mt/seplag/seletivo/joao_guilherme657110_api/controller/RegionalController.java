package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.controller;

import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto.RegionalRequest;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto.RegionalResponse;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.service.RegionalService;
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
public class RegionalController {

    private final RegionalService service;

    @GetMapping
    public ResponseEntity<Page<RegionalResponse>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String filtro) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nome"));
        return ResponseEntity.ok(service.findAll(pageable, filtro));
    }

    @GetMapping("/ativas")
    public ResponseEntity<List<RegionalResponse>> listAtivas() {
        return ResponseEntity.ok(service.findAllAtivas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegionalResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(service.count());
    }

    @PostMapping
    public ResponseEntity<RegionalResponse> create(@Validated @RequestBody RegionalRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RegionalResponse> update(@PathVariable Long id, @Validated @RequestBody RegionalRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
