package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.repository;

import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.entity.Regional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RegionalRepository extends JpaRepository<Regional, Long>, JpaSpecificationExecutor<Regional> {
    Optional<Regional> findByNomeIgnoreCase(String nome);

    List<Regional> findByAtivoTrueOrderByNome();

    @Query("SELECT r FROM Regional r ORDER BY r.nome")
    Page<Regional> findAllOrdered(Pageable pageable);
}
