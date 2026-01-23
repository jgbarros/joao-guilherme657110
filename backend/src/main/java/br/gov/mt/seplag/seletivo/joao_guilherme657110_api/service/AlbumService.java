package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.service;

import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.config.handler.AlbumWebSocketHandler;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto.ws.AlbumNotification;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto.AlbumRequest;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto.AlbumResponse;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.entity.Album;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.entity.Artista;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.entity.Regional;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.mapper.AlbumMapper;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.repository.AlbumRepository;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.repository.ArtistaRepository;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.repository.RegionalRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AlbumService {

    private final AlbumRepository repository;
    private final ArtistaRepository artistaRepository;
    private final RegionalRepository regionalRepository;
    private final AlbumMapper mapper;
    private final AlbumWebSocketHandler webSocketHandler;

    public Page<AlbumResponse> findAll(Pageable pageable, String filtro) {
        if (filtro != null && !filtro.isBlank()) {
            return repository.findAll(
                            (root, query, cb) -> cb.like(cb.lower(root.get("titulo")),
                                    "%" + filtro.toLowerCase() + "%"),
                            pageable)
                    .map(mapper::toResponse);
        }
        return repository.findAllOrdered(pageable).map(mapper::toResponse);
    }

    public AlbumResponse findById(Long id) {
        Album album = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Álbum não encontrado"));
        return mapper.toResponse(album);
    }

    public long count() {
        return repository.count();
    }

    public AlbumResponse create(AlbumRequest request) {
        Album album = new Album();
        updateEntity(album, request);
        Album savedAlbum = repository.save(album);

        // Prepara a notificação
        Map<String, Object> notification = new HashMap<>();
        notification.put("type", "ALBUM_CRIADO");
        notification.put("data", AlbumNotification.builder()
                .albumId(savedAlbum.getId())
                .albumTitle(savedAlbum.getTitulo())
                .artistName(savedAlbum.getArtista().getNome())
                .createdAt(LocalDateTime.now())
                .message("Novo álbum cadastrado com sucesso!")
                .build());

        // Envia notificação via WebSocket Nativo
        webSocketHandler.broadcast(notification);

        return mapper.toResponse(savedAlbum);
    }

    public AlbumResponse update(Long id, AlbumRequest request) {
        Album album = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Álbum não encontrado"));
        updateEntity(album, request);
        return mapper.toResponse(repository.save(album));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public void updateCapa(Long id, String url) {
        Album album = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Álbum não encontrado"));
        album.setCapaUrl(url);
        repository.save(album);
    }

    private void updateEntity(Album album, AlbumRequest req) {
        album.setTitulo(req.getTitulo());
        album.setAnoLancamento(req.getAnoLancamento());
        album.setGenero(req.getGenero());
        album.setCapaUrl(req.getCapaUrl());
        album.setFaixas(req.getFaixas());

        Artista artista = artistaRepository.findById(req.getArtistaId())
                .orElseThrow(() -> new EntityNotFoundException("Artista não encontrado"));
        album.setArtista(artista);

        if (req.getRegionalId() != null) {
            Regional regional = regionalRepository.findById(req.getRegionalId())
                    .orElseThrow(() -> new EntityNotFoundException("Regional não encontrada"));
            album.setRegional(regional);
        } else {
            album.setRegional(null);
        }
    }
}
