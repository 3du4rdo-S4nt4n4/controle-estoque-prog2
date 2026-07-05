package com.uemg.estoque.dto;

public record FornecedorResponseDTO(
        Long id,
        String nome,
        String cnpj,
        String telefone,
        String email,
        String endereco
) {
}
