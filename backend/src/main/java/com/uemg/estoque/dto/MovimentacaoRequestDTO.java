package com.uemg.estoque.dto;

import com.uemg.estoque.model.TipoMovimentacao;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record MovimentacaoRequestDTO(

        @NotNull(message = "O produto é obrigatório")
        Long produtoId,

        @NotNull(message = "O tipo de movimentação é obrigatório (ENTRADA ou SAIDA)")
        TipoMovimentacao tipo,

        @NotNull(message = "A quantidade é obrigatória")
        @Positive(message = "A quantidade deve ser maior que zero")
        Integer quantidade,

        @Size(max = 255)
        String motivo
) {
}
