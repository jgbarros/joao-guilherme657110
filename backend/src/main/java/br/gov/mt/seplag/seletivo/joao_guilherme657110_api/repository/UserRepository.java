package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.repository;

import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}