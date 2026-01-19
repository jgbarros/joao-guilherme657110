package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.repository;

import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.entity.Album;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface AlbumRepository extends JpaRepository<Album, Long>, JpaSpecificationExecutor<Album> {
    
    @Query("SELECT a FROM Album a JOIN FETCH a.artista LEFT JOIN FETCH a.regional ORDER BY a.titulo")
    Page<Album> findAllOrdered(Pageable pageable);
}
