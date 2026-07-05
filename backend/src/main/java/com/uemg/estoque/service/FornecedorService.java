package com.uemg.estoque.service;

import com.uemg.estoque.dto.FornecedorRequestDTO;
import com.uemg.estoque.dto.FornecedorResponseDTO;
import com.uemg.estoque.exception.BusinessException;
import com.uemg.estoque.exception.ResourceNotFoundException;
import com.uemg.estoque.model.Fornecedor;
import com.uemg.estoque.repository.FornecedorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FornecedorService {

    private final FornecedorRepository fornecedorRepository;

    @Transactional(readOnly = true)
    public List<FornecedorResponseDTO> listarTodos() {
        return fornecedorRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public FornecedorResponseDTO buscarPorId(Long id) {
        return toResponseDTO(buscarEntidadePorId(id));
    }

    public FornecedorResponseDTO criar(FornecedorRequestDTO dto) {
        validarCnpjDuplicado(dto.cnpj(), null);

        Fornecedor fornecedor = new Fornecedor();
        aplicarDados(fornecedor, dto);
        return toResponseDTO(fornecedorRepository.save(fornecedor));
    }

    public FornecedorResponseDTO atualizar(Long id, FornecedorRequestDTO dto) {
        Fornecedor fornecedor = buscarEntidadePorId(id);
        validarCnpjDuplicado(dto.cnpj(), id);

        aplicarDados(fornecedor, dto);
        return toResponseDTO(fornecedorRepository.save(fornecedor));
    }

    public void deletar(Long id) {
        Fornecedor fornecedor = buscarEntidadePorId(id);
        fornecedorRepository.delete(fornecedor);
    }

    private void validarCnpjDuplicado(String cnpj, Long idAtual) {
        if (cnpj == null || cnpj.isBlank()) {
            return;
        }
        fornecedorRepository.findByCnpj(cnpj)
                .filter(f -> idAtual == null || !f.getId().equals(idAtual))
                .ifPresent(f -> {
                    throw new BusinessException("Já existe um fornecedor cadastrado com o CNPJ " + cnpj);
                });
    }

    private void aplicarDados(Fornecedor fornecedor, FornecedorRequestDTO dto) {
        fornecedor.setNome(dto.nome());
        fornecedor.setCnpj(dto.cnpj());
        fornecedor.setTelefone(dto.telefone());
        fornecedor.setEmail(dto.email());
        fornecedor.setEndereco(dto.endereco());
    }

    private Fornecedor buscarEntidadePorId(Long id) {
        return fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Fornecedor com id " + id + " não encontrado"));
    }

    private FornecedorResponseDTO toResponseDTO(Fornecedor f) {
        return new FornecedorResponseDTO(f.getId(), f.getNome(), f.getCnpj(), f.getTelefone(), f.getEmail(), f.getEndereco());
    }
}
