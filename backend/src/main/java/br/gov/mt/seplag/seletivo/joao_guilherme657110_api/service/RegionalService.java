package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.service;

import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto.RegionalExternalDto;
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
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
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

    public void syncFromExternalApi() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://integrador-argus-api.geia.vip/v1/regionais";
        RegionalExternalDto[] externalRegionais = restTemplate.getForObject(url, RegionalExternalDto[].class);

        if (externalRegionais != null) {
            for (RegionalExternalDto dto : externalRegionais) {
                Regional regional = repository.findById(dto.getId())
                        .orElse(new Regional());
                regional.setId(dto.getId());
                regional.setNome(dto.getNome());
                regional.setAtivo(true);
                repository.save(regional);
            }
        }
    }
}
