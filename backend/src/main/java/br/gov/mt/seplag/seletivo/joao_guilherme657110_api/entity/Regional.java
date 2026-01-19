package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "regionais")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Regional {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nome;

    private Boolean ativo = true;
}
