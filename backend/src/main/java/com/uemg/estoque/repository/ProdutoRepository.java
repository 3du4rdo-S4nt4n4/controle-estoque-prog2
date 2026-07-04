package com.uemg.estoque.repository;

import com.uemg.estoque.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    boolean existsByCodigoBarras(String codigoBarras);

    Optional<Produto> findByCodigoBarras(String codigoBarras);

    List<Produto> findByCategoriaId(Long categoriaId);

    List<Produto> findByFornecedorId(Long fornecedorId);

    List<Produto> findByAtivoTrue();

    // Usado futuramente para alertas de estoque baixo (quantidade <= quantidade minima)
    @Query("SELECT p FROM Produto p WHERE p.quantidadeEstoque <= p.quantidadeMinima AND p.ativo = true")
    List<Produto> findProdutosComEstoqueBaixo();
}
