package com.uemg.estoque.service;

import com.uemg.estoque.dto.CategoriaRequestDTO;
import com.uemg.estoque.dto.CategoriaResponseDTO;
import com.uemg.estoque.exception.BusinessException;
import com.uemg.estoque.exception.ResourceNotFoundException;
import com.uemg.estoque.model.Categoria;
import com.uemg.estoque.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listarTodas() {
        return categoriaRepository.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponseDTO buscarPorId(Long id) {
        return toResponseDTO(buscarEntidadePorId(id));
    }

    public CategoriaResponseDTO criar(CategoriaRequestDTO dto) {
        if (categoriaRepository.existsByNomeIgnoreCase(dto.nome())) {
            throw new BusinessException("Já existe uma categoria com o nome '" + dto.nome() + "'");
        }
        Categoria categoria = new Categoria();
        categoria.setNome(dto.nome());
        categoria.setDescricao(dto.descricao());
        return toResponseDTO(categoriaRepository.save(categoria));
    }

    public CategoriaResponseDTO atualizar(Long id, CategoriaRequestDTO dto) {
        Categoria categoria = buscarEntidadePorId(id);

        boolean nomeMudou = !categoria.getNome().equalsIgnoreCase(dto.nome());
        if (nomeMudou && categoriaRepository.existsByNomeIgnoreCase(dto.nome())) {
            throw new BusinessException("Já existe uma categoria com o nome '" + dto.nome() + "'");
        }

        categoria.setNome(dto.nome());
        categoria.setDescricao(dto.descricao());
        return toResponseDTO(categoriaRepository.save(categoria));
    }

    public void deletar(Long id) {
        Categoria categoria = buscarEntidadePorId(id);
        categoriaRepository.delete(categoria);
    }

    private Categoria buscarEntidadePorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Categoria com id " + id + " não encontrada"));
    }

    private CategoriaResponseDTO toResponseDTO(Categoria categoria) {
        return new CategoriaResponseDTO(categoria.getId(), categoria.getNome(), categoria.getDescricao());
    }
}
