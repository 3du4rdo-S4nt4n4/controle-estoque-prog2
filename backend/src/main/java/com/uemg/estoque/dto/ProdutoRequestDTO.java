package com.uemg.estoque.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * DTO recebido do front-end para criar ou atualizar um Produto.
 * Nao expoe o id (vem da URL na edicao) nem dataCadastro (preenchida pelo backend).
 */
public record ProdutoRequestDTO(

        @NotBlank(message = "O nome do produto é obrigatório")
        @Size(max = 150)
        String nome,

        @Size(max = 255)
        String descricao,

        @Size(max = 50)
        String codigoBarras,

        Long categoriaId,

        Long fornecedorId,
        //Todas essas infos tem que ser obrigatprias, evita dados "sujos" no banco
        @NotNull(message = "O preço de custo é obrigatório")
        @PositiveOrZero(message = "O preço de custo não pode ser negativo")
        BigDecimal precoCusto,

        @NotNull(message = "O preço de venda é obrigatório")
        @PositiveOrZero(message = "O preço de venda não pode ser negativo")
        BigDecimal precoVenda,

        @NotNull(message = "A quantidade em estoque é obrigatória")
        @PositiveOrZero(message = "A quantidade em estoque não pode ser negativa")
        Integer quantidadeEstoque,

        @NotNull(message = "A quantidade mínima é obrigatória")
        @PositiveOrZero(message = "A quantidade mínima não pode ser negativa")
        Integer quantidadeMinima,

        @NotBlank(message = "A unidade de medida é obrigatória")
        @Size(max = 10)
        String unidadeMedida,

        Boolean ativo
) {
}
