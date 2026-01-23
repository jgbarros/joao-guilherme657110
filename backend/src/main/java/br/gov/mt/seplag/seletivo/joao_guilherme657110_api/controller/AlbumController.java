package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.controller;

import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto.AlbumRequest;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto.AlbumResponse;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.service.AlbumService;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.service.MinioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/albuns")
@RequiredArgsConstructor
public class AlbumController {

    private final AlbumService service;
    private final MinioService minioService;

    @Value("${minio.bucket}")
    private String bucketName;
    
    @GetMapping
    public ResponseEntity<Page<AlbumResponse>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String filtro) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("titulo"));
        return ResponseEntity.ok(service.findAll(pageable, filtro));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlbumResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(service.count());
    }

    @PostMapping
    public ResponseEntity<AlbumResponse> create(@Validated @RequestBody AlbumRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlbumResponse> update(@PathVariable Long id, @Validated @RequestBody AlbumRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/upload")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> uploadCapa(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            String url = minioService.uploadFile(file, bucketName, "albuns/" + id + "/" + file.getOriginalFilename());
            service.updateCapa(id, url);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro upload: " + e.getMessage());
        }
    }
}
