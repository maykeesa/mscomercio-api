package br.com.ms.pagamento.service;

import br.com.ms.pagamento.dto.PagamentoDto;
import org.springframework.data.domain.Pageable;

public interface PagamentoService {

    Object buscar(String id, String pagamentoId, Pageable pageable);

    PagamentoDto.Response.Pagamento cadastrar(PagamentoDto.Request.Pagamento dto);

    PagamentoDto.Response.Pagamento validarPagamento(PagamentoDto.Request.Validar dto);
}
