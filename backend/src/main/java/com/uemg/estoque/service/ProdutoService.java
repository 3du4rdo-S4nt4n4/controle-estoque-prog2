package com.uemg.estoque.service;

import com.uemg.estoque.dto.CategoriaSimplesDTO;
import com.uemg.estoque.dto.FornecedorSimplesDTO;
import com.uemg.estoque.dto.ProdutoRequestDTO;
import com.uemg.estoque.dto.ProdutoResponseDTO;
import com.uemg.estoque.exception.BusinessException;
import com.uemg.estoque.exception.ResourceNotFoundException;
import com.uemg.estoque.model.Categoria;
import com.uemg.estoque.model.Fornecedor;
import com.uemg.estoque.model.Produto;
import com.uemg.estoque.repository.CategoriaRepository;
import com.uemg.estoque.repository.FornecedorRepository;
import com.uemg.estoque.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaRepository categoriaRepository;
    private final FornecedorRepository fornecedorRepository;

    @Transactional(readOnly = true)
    public List<ProdutoResponseDTO> listarTodos() {
        return produtoRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    
    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarPorId(Long id) {
        Produto produto = buscarEntidadePorId(id);
        return toResponseDTO(produto);
    }

    
    public ProdutoResponseDTO criar(ProdutoRequestDTO dto) {
        validarCodigoBarrasDuplicado(dto.codigoBarras(), null);

        Produto produto = new Produto();
        aplicarDadosNaEntidade(produto, dto);

        Produto salvo = produtoRepository.save(produto);
        return toResponseDTO(salvo);
    }

    public ProdutoResponseDTO atualizar(Long id, ProdutoRequestDTO dto) {
        Produto produto = buscarEntidadePorId(id);
        validarCodigoBarrasDuplicado(dto.codigoBarras(), id);

        aplicarDadosNaEntidade(produto, dto);

        Produto atualizado = produtoRepository.save(produto);
        return toResponseDTO(atualizado);
    }

    public void deletar(Long id) {
        Produto produto = buscarEntidadePorId(id);
        produtoRepository.delete(produto);
    }

    
    // Metodos auxiliares privados

    //Não atualiza ou deleta sem buscar
    private Produto buscarEntidadePorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produto com id " + id + " não encontrado"));
    }

    //Nao deixa valida sprodutos iguais
    private void validarCodigoBarrasDuplicado(String codigoBarras, Long idAtual) {
        if (codigoBarras == null || codigoBarras.isBlank()) {
            return;
        }
        produtoRepository.findByCodigoBarras(codigoBarras)
                .filter(p -> idAtual == null || !p.getId().equals(idAtual))
                .ifPresent(p -> {
                    throw new BusinessException(
                            "Já existe um produto cadastrado com o código de barras " + codigoBarras);
                });
    }

    private void aplicarDadosNaEntidade(Produto produto, ProdutoRequestDTO dto) {
        produto.setNome(dto.nome());
        produto.setDescricao(dto.descricao());
        produto.setCodigoBarras(dto.codigoBarras());
        produto.setPrecoCusto(dto.precoCusto());
        produto.setPrecoVenda(dto.precoVenda());
        produto.setQuantidadeEstoque(dto.quantidadeEstoque());
        produto.setQuantidadeMinima(dto.quantidadeMinima());
        produto.setUnidadeMedida(dto.unidadeMedida());
        produto.setAtivo(dto.ativo() != null ? dto.ativo() : Boolean.TRUE);

        if (dto.categoriaId() != null) {
            Categoria categoria = categoriaRepository.findById(dto.categoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Categoria com id " + dto.categoriaId() + " não encontrada"));
            produto.setCategoria(categoria);
        } else {
            produto.setCategoria(null);
        }

        if (dto.fornecedorId() != null) {
            Fornecedor fornecedor = fornecedorRepository.findById(dto.fornecedorId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Fornecedor com id " + dto.fornecedorId() + " não encontrado"));
            produto.setFornecedor(fornecedor);
        } else {
            produto.setFornecedor(null);
        }
    }

    private ProdutoResponseDTO toResponseDTO(Produto p) {
        CategoriaSimplesDTO categoriaDTO = p.getCategoria() != null
                ? new CategoriaSimplesDTO(p.getCategoria().getId(), p.getCategoria().getNome())
                : null;

        FornecedorSimplesDTO fornecedorDTO = p.getFornecedor() != null
                ? new FornecedorSimplesDTO(p.getFornecedor().getId(), p.getFornecedor().getNome())
                : null;

                //calculo dinamico pra ficar smepre atualizado
        boolean estoqueBaixo = p.getQuantidadeEstoque() != null
                && p.getQuantidadeMinima() != null
                && p.getQuantidadeEstoque() <= p.getQuantidadeMinima();

        return new ProdutoResponseDTO(
                p.getId(),
                p.getNome(),
                p.getDescricao(),
                p.getCodigoBarras(),
                categoriaDTO,
                fornecedorDTO,
                p.getPrecoCusto(),
                p.getPrecoVenda(),
                p.getQuantidadeEstoque(),
                p.getQuantidadeMinima(),
                p.getUnidadeMedida(),
                p.getDataCadastro(),
                p.getAtivo(),
                estoqueBaixo
        );
    }
}
