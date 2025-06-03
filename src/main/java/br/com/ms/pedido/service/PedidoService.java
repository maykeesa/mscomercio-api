package br.com.ms.pedido.service;

import br.com.ms.pedido.dto.PedidoDto;
import org.springframework.data.domain.Pageable;

public interface PedidoService {

    Object buscar(String id, Pageable pageable);

    PedidoDto.Response.Pedido cadastrar(PedidoDto.Request.Pedido dto);
}
