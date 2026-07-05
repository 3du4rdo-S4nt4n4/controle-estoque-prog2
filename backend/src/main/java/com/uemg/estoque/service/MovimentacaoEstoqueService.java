package com.uemg.estoque.service;

import com.uemg.estoque.dto.MovimentacaoRequestDTO;
import com.uemg.estoque.dto.MovimentacaoResponseDTO;
import com.uemg.estoque.exception.BusinessException;
import com.uemg.estoque.exception.ResourceNotFoundException;
import com.uemg.estoque.model.MovimentacaoEstoque;
import com.uemg.estoque.model.Produto;
import com.uemg.estoque.model.TipoMovimentacao;
import com.uemg.estoque.repository.MovimentacaoEstoqueRepository;
import com.uemg.estoque.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MovimentacaoEstoqueService {

    private final MovimentacaoEstoqueRepository movimentacaoRepository;
    private final ProdutoRepository produtoRepository;

    /**
     * Registra uma movimentacao (entrada ou saida) e atualiza a quantidade
     * em estoque do produto na MESMA transacao. Se a saida deixar o estoque
     * negativo, a operacao inteira e revertida (rollback automatico, ja que
     * a classe e @Transactional) e uma BusinessException e lancada.
     */
    public MovimentacaoResponseDTO registrar(MovimentacaoRequestDTO dto) {
        Produto produto = produtoRepository.findById(dto.produtoId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produto com id " + dto.produtoId() + " não encontrado"));

        int novaQuantidade = calcularNovaQuantidade(produto, dto);

        produto.setQuantidadeEstoque(novaQuantidade);
        produtoRepository.save(produto);

        MovimentacaoEstoque movimentacao = new MovimentacaoEstoque();
        movimentacao.setProduto(produto);
        movimentacao.setTipo(dto.tipo());
        movimentacao.setQuantidade(dto.quantidade());
        movimentacao.setMotivo(dto.motivo());

        MovimentacaoEstoque salva = movimentacaoRepository.save(movimentacao);
        return toResponseDTO(salva, produto.getQuantidadeEstoque());
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoResponseDTO> listarPorProduto(Long produtoId) {
        if (!produtoRepository.existsById(produtoId)) {
            throw new ResourceNotFoundException("Produto com id " + produtoId + " não encontrado");
        }
        return movimentacaoRepository.findByProdutoIdOrderByDataMovimentacaoDesc(produtoId).stream()
                .map(m -> toResponseDTO(m, m.getProduto().getQuantidadeEstoque()))
                .toList();
    }

    private int calcularNovaQuantidade(Produto produto, MovimentacaoRequestDTO dto) {
        int atual = produto.getQuantidadeEstoque();

        if (dto.tipo() == TipoMovimentacao.ENTRADA) {
            return atual + dto.quantidade();
        }

        // SAIDA
        int resultado = atual - dto.quantidade();
        if (resultado < 0) {
            throw new BusinessException(
                    "Estoque insuficiente para o produto '" + produto.getNome() +
                    "'. Disponível: " + atual + ", solicitado: " + dto.quantidade());
        }
        return resultado;
    }

    private MovimentacaoResponseDTO toResponseDTO(MovimentacaoEstoque m, Integer quantidadeEstoqueAtual) {
        return new MovimentacaoResponseDTO(
                m.getId(),
                m.getProduto().getId(),
                m.getProduto().getNome(),
                m.getTipo(),
                m.getQuantidade(),
                m.getMotivo(),
                m.getDataMovimentacao(),
                quantidadeEstoqueAtual
        );
    }
}
