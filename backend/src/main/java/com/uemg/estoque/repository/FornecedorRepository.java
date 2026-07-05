package com.uemg.estoque.repository;

import com.uemg.estoque.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {

    boolean existsByCnpj(String cnpj);

    Optional<Fornecedor> findByCnpj(String cnpj);
}
