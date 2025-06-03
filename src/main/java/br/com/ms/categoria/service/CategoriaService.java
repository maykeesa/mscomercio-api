package br.com.ms.categoria.service;

import br.com.ms.categoria.dto.CategoriaDto;
import org.springframework.data.domain.Pageable;

public interface CategoriaService {

    Object buscar(String id, Pageable pageable);

    CategoriaDto.Response.Categoria cadastrar(CategoriaDto.Request.Categoria dto);
}
