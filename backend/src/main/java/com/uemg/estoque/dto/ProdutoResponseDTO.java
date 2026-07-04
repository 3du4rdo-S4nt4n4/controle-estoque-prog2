package com.uemg.estoque.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//Angelugar só recebe o necessário
//Exibe Categoria e Fornecedor
public record ProdutoResponseDTO(
        Long id,
        String nome,
        String descricao,
        String codigoBarras,
        CategoriaSimplesDTO categoria,
        FornecedorSimplesDTO fornecedor,
        BigDecimal precoCusto,
        BigDecimal precoVenda,
        Integer quantidadeEstoque,
        Integer quantidadeMinima,
        String unidadeMedida,
        LocalDateTime dataCadastro,
        Boolean ativo,
        Boolean estoqueBaixo
) {
}
