package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.controller;

import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto.AlbumRequest;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto.AlbumResponse;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.service.AlbumService;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.service.MinioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/albuns")
@RequiredArgsConstructor
@Tag(name = "Álbuns", description = "Gerenciamento de Álbuns")
public class AlbumController {

    private final AlbumService service;
    private final MinioService minioService;

    @Value("${minio.bucket}")
    private String bucketName;
    
    @GetMapping
    @Operation(summary = "Listar álbuns", description = "Retorna uma lista paginada de álbuns")
    public ResponseEntity<Page<AlbumResponse>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String filtro) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("titulo"));
        return ResponseEntity.ok(service.findAll(pageable, filtro));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar álbum por ID", description = "Retorna um álbum específico pelo seu ID")
    public ResponseEntity<AlbumResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/count")
    @Operation(summary = "Contar álbuns", description = "Retorna a quantidade total de álbuns cadastrados")
    public ResponseEntity<Long> count() {
        return ResponseEntity.ok(service.count());
    }

    @PostMapping
    @Operation(summary = "Criar álbum", description = "Cria um novo álbum")
    public ResponseEntity<AlbumResponse> create(@Validated @RequestBody AlbumRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(req));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar álbum", description = "Atualiza os dados de um álbum existente")
    public ResponseEntity<AlbumResponse> update(@PathVariable Long id, @Validated @RequestBody AlbumRequest req) {
        return ResponseEntity.ok(service.update(id, req));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar álbum", description = "Remove um álbum pelo seu ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/upload")
    @Operation(summary = "Upload da capa do álbum", description = "Faz o upload da imagem de capa para um álbum")
    @PreAuthorize("hasRole('ADMIN')")
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
