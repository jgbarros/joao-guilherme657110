package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.repository;

import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.entity.Artista;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ArtistaRepository extends JpaRepository<Artista, Long>, JpaSpecificationExecutor<Artista> {
    Optional<Artista> findByNomeIgnoreCase(String nome);

    @Query("SELECT a FROM Artista a ORDER BY a.nome")
    Page<Artista> findAllOrdered(Pageable pageable);
}
