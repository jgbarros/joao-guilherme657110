package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.service;

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

@Service
@RequiredArgsConstructor
@Transactional
public class ArtistaService {

    private final ArtistaRepository repository;
    private final ArtistaMapper mapper;

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

    public ArtistaResponse create(ArtistaRequest request) {
        Artista artista = new Artista();
        updateEntity(artista, request);
        return mapper.toResponse(repository.save(artista));
    }

    public ArtistaResponse update(Long id, ArtistaRequest request) {
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
        //artista.setNacionalidade(req.getNacionalidade());
        //artista.setDataNascimento(req.getDataNascimento());
        // ...
    }
}
