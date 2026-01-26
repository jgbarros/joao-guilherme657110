package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.service;

import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.config.handler.ArtistaWebSocketHandler;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto.ws.ArtistaNotification;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.mapper.ArtistaMapper;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto.ArtistaRequest;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto.ArtistaResponse;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.entity.Artista;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.repository.ArtistaRepository;
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
public class ArtistaService {

    private final ArtistaRepository repository;
    private final ArtistaMapper mapper;
    private final ArtistaWebSocketHandler webSocketHandler;

    public Page<ArtistaResponse> findAll(Pageable pageable, String filtro) {
        if (filtro != null && !filtro.isBlank()) {
            return repository.findAll(
                            (root, query, cb) -> cb.like(cb.lower(root.get("nome")),
                                    "%" + filtro.toLowerCase() + "%"),
                            pageable)
                    .map(mapper::toResponse);
        }
        return repository.findAllOrdered(pageable).map(mapper::toResponse);
    }

    public ArtistaResponse findById(Long id) {
        Artista artista = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Artista não encontrado"));
        return mapper.toResponse(artista);
    }

    public long count() {
        return repository.count();
    }

    public ArtistaResponse create(ArtistaRequest request) {
        validateDateRange(request);
        Artista artista = new Artista();
        updateEntity(artista, request);
        Artista savedArtista = repository.save(artista);

        // Prepara a notificação
        Map<String, Object> notification = new HashMap<>();
        notification.put("type", "ARTISTA_CRIADO");
        notification.put("data", ArtistaNotification.builder()
                .artistId(savedArtista.getId())
                .artistName(savedArtista.getNome())
                .createdAt(LocalDateTime.now())
                .message("Novo artista cadastrado com sucesso!")
                .build());

        // Envia notificação via WebSocket Nativo
        webSocketHandler.broadcast(notification);

        return mapper.toResponse(savedArtista);
    }

    public ArtistaResponse update(Long id, ArtistaRequest request) {
        validateDateRange(request);
        ArtistaResponse artistaResponse = findById(id);
        Artista artista = mapper.toEntity(artistaResponse);
        updateEntity(artista, request);
        return mapper.toResponse(repository.save(artista));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    private void updateEntity(Artista artista, ArtistaRequest req) {
        artista.setNome(req.getNome());
        artista.setNacionalidade(req.getNacionalidade());
        artista.setDataNascimento(req.getDataNascimento());
        artista.setDataMorte(req.getDataMorte());
        artista.setBiografia(req.getBiografia());
    }

    private void validateDateRange(ArtistaRequest request) {
        if (request.getDataNascimento() != null && request.getDataMorte() != null) {
            if (request.getDataNascimento().isAfter(request.getDataMorte())) {
                throw new IllegalArgumentException("A data de nascimento não pode ser posterior à data de morte.");
            }
        }
    }
}
