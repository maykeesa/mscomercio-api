package br.com.ms.transacao.service;

import br.com.ms.transacao.Transacao;
import br.com.ms.transacao.dto.TransacaoDto;

import java.util.List;
import java.util.UUID;

public interface TransacaoService {

    Transacao buscar(UUID id);

    List<Transacao> buscarTransacoes(UUID pagamentoId);

    Transacao cadastrar(TransacaoDto.Request.Transacao dto);
}

