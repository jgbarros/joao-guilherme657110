package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.service;

import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto.RegionalRequest;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto.RegionalResponse;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.entity.Regional;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.mapper.RegionalMapper;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.repository.RegionalRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RegionalService {

    private final RegionalRepository repository;
    private final RegionalMapper mapper;

    public Page<RegionalResponse> findAll(Pageable pageable, String filtro) {
        if (filtro != null && !filtro.isBlank()) {
            return repository.findAll(
                            (root, query, cb) -> cb.like(cb.lower(root.get("nome")),
                                    "%" + filtro.toLowerCase() + "%"),
                            pageable)
                    .map(mapper::toResponse);
        }
        return repository.findAllOrdered(pageable).map(mapper::toResponse);
    }

    public List<RegionalResponse> findAllAtivas() {
        return repository.findByAtivoTrueOrderByNome().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public RegionalResponse findById(Long id) {
        Regional regional = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Regional não encontrada"));
        return mapper.toResponse(regional);
    }

    public long count() {
        return repository.count();
    }

    public RegionalResponse create(RegionalRequest request) {
        Regional regional = new Regional();
        updateEntity(regional, request);
        return mapper.toResponse(repository.save(regional));
    }

    public RegionalResponse update(Long id, RegionalRequest request) {
        Regional regional = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Regional não encontrada"));
        updateEntity(regional, request);
        return mapper.toResponse(repository.save(regional));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    private void updateEntity(Regional regional, RegionalRequest req) {
        regional.setNome(req.getNome());
        regional.setAtivo(req.getAtivo());
    }
}
