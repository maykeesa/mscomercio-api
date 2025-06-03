package br.com.ms.produto.service;

import br.com.ms.produto.Produto;
import br.com.ms.produto.dto.ProdutoDto;
import br.com.ms.produto.repository.ProdutoRepository;
import br.com.ms.produto.service.utils.ProdutoServiceUtils;
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
public class ProdutoServiceImpl implements ProdutoService{

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ProdutoServiceUtils produtoServiceUtils;

    @Override
    public Object buscar(String id, Pageable pageable) {
        if(Objects.nonNull(id)){
            return DtoService.entityToDto(produtoServiceUtils
                    .buscarProduto(UUID.fromString(id)), ProdutoDto.Response.Produto.class);
        }

        Page<Produto> produtos = this.produtoRepository.findAll(pageable);
        return new PageImpl<>(DtoService.entitysToDtos(produtos.getContent(), ProdutoDto.Response.Produto.class));
    }

    @Override
    public ProdutoDto.Response.Produto cadastrar(ProdutoDto.Request.Produto dto) {
        Produto produto = DtoService.dtoToEntity(dto, Produto.class);
        Produto produtoPersistido = this.produtoRepository.save(produto);

        return DtoService.entityToDto(produtoPersistido, ProdutoDto.Response.Produto.class);
    }

    @Override
    public void deletar(String id) {
        Produto produto = this.produtoRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new EntityNotFoundException(ENTIDADE_NAO_ENCONTRADA.getDescricao()));

        this.produtoRepository.delete(produto);
    }
}
