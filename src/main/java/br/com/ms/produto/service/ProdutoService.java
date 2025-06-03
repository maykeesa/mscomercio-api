package br.com.ms.produto.service;

import br.com.ms.produto.dto.ProdutoDto;
import org.springframework.data.domain.Pageable;

public interface ProdutoService {

    Object buscar(String id, Pageable pageable);

    ProdutoDto.Response.Produto cadastrar(ProdutoDto.Request.Produto dto);

    void deletar(String id);
}
