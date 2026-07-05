package com.uemg.estoque.controller;

import com.uemg.estoque.dto.MovimentacaoRequestDTO;
import com.uemg.estoque.dto.MovimentacaoResponseDTO;
import com.uemg.estoque.service.MovimentacaoEstoqueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimentacoes")
@RequiredArgsConstructor
public class MovimentacaoEstoqueController {

    private final MovimentacaoEstoqueService movimentacaoService;

    // POST /movimentacoes - registra uma entrada ou saida de estoque
    @PostMapping
    public ResponseEntity<MovimentacaoResponseDTO> registrar(@Valid @RequestBody MovimentacaoRequestDTO dto) {
        MovimentacaoResponseDTO registrada = movimentacaoService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registrada);
    }

    // GET /movimentacoes/produto/{produtoId} - historico de movimentacoes de um produto
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<List<MovimentacaoResponseDTO>> listarPorProduto(@PathVariable Long produtoId) {
        return ResponseEntity.ok(movimentacaoService.listarPorProduto(produtoId));
    }
}
