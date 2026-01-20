package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto.security;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;

    public AuthResponse(String token) {
        this.token = token;
    }
}