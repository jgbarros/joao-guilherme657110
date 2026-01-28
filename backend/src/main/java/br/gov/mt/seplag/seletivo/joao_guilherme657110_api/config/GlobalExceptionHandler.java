package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.config;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        Map<String, String> response = new HashMap<>();
        String message = ex.getMostSpecificCause().getMessage();
        
        if (message != null && message.contains("duplicate key value violates unique constraint")) {
            response.put("error", "Conflito de dados");
            response.put("message", "Já existe um registro com este nome.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        response.put("error", "Erro de integridade de dados");
        response.put("message", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
