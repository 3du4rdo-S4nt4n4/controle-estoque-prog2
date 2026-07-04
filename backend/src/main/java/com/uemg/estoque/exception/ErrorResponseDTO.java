package com.uemg.estoque.exception;

import java.time.LocalDateTime;
import java.util.List;

//define quais informações, envia para a tela
public record ErrorResponseDTO(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        List<String> detalhes
) {
    public ErrorResponseDTO(int status, String error, String message) {
        this(LocalDateTime.now(), status, error, message, null);
    }

    public ErrorResponseDTO(int status, String error, String message, List<String> detalhes) {
        this(LocalDateTime.now(), status, error, message, detalhes);
    }
}
