package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.controller;

import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto.ArtistaDetalheResponse;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto.ArtistaRequest;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto.ArtistaResponse;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.service.ArtistaService;
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

@RestController
@RequestMapping("/api/artistas")
@RequiredArgsConstructor
@Tag(name = "Artistas", description = "Gerenciamento de Artistas")
public class ArtistaController {

    private final ArtistaService service;

    @GetMapping
    @Operation(summary = "Listar artistas", description = "Retorna uma lista paginada de artistas")
    public ResponseEntity<Page<ArtistaResponse>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String filtro) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nome"));
        return ResponseEntity.ok(service.findAll(pageable, filtro));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar artista por ID", description = "Retorna um artista específico pelo seu ID")
    public ResponseEntity<ArtistaResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/{id}/detalhe")
    @Operation(summary = "Buscar detalhes do artista", description = "Retorna um artista com a lista de seus álbuns")
    public ResponseEntity<ArtistaDetalheResponse> findDetalheById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findDetalheById(id));
    }

    @GetMapping("/count")
    @Operation(summary = "Contar artistas", description = "Retorna a quantidade total de artistas cadastrados")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(service.count());
    }

    @PostMapping
    @Operation(summary = "Criar artista", description = "Cria um novo artista")
    public ResponseEntity<ArtistaResponse> create(@Validated @RequestBody ArtistaRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar artista", description = "Atualiza os dados de um artista existente")
    public ResponseEntity<ArtistaResponse> update(@PathVariable Long id, @Validated @RequestBody ArtistaRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar artista", description = "Remove um artista pelo seu ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
