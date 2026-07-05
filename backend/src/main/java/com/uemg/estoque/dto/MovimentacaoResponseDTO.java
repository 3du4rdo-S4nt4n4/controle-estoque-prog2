package com.uemg.estoque.dto;

import com.uemg.estoque.model.TipoMovimentacao;

import java.time.LocalDateTime;

public record MovimentacaoResponseDTO(
        Long id,
        Long produtoId,
        String produtoNome,
        TipoMovimentacao tipo,
        Integer quantidade,
        String motivo,
        LocalDateTime dataMovimentacao,
        Integer quantidadeEstoqueAtual
) {
}
