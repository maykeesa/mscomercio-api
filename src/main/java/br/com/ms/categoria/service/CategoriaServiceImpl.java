package br.com.ms.categoria.service;

import br.com.ms.categoria.Categoria;
import br.com.ms.categoria.dto.CategoriaDto;
import br.com.ms.categoria.repository.CategoriaRepository;
import br.com.ms.utils.service.DtoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

import static br.com.ms.config.exception.enums.MensagensException.ENTIDADE_NAO_ENCONTRADA;

@Service
public class CategoriaServiceImpl implements CategoriaService{

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Override
    public Object buscar(String id, Pageable pageable) {
        if(Objects.nonNull(id)){
            Categoria categoria = categoriaRepository.findById(UUID.fromString(id))
                    .orElseThrow(() -> new EntityNotFoundException(ENTIDADE_NAO_ENCONTRADA.getDescricao()));

            return DtoService.entityToDto(categoria, CategoriaDto.Response.Categoria.class);
        }

        Page<Categoria> categorias = categoriaRepository.findAll(pageable);
        return new PageImpl<>(DtoService.entitysToDtos(categorias.getContent(), CategoriaDto.Response.Categoria.class));
    }

    @Override
    public CategoriaDto.Response.Categoria cadastrar(CategoriaDto.Request.Categoria dto) {
        Categoria categoria = DtoService.dtoToEntity(dto, Categoria.class);
        Categoria categoriaPersistido = this.categoriaRepository.save(categoria);

        return DtoService.entityToDto(categoriaPersistido, CategoriaDto.Response.Categoria.class);
    }
}
