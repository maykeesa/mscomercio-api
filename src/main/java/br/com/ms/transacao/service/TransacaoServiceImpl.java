package br.com.ms.transacao.service;

import br.com.ms.config.exception.exceptions.ServiceException;
import br.com.ms.pagamento.Pagamento;
import br.com.ms.pagamento.service.utils.PagamentoServiceUtils;
import br.com.ms.transacao.Transacao;
import br.com.ms.transacao.dto.TransacaoDto;
import br.com.ms.transacao.repository.TransacaoRepository;
import br.com.ms.utils.service.DtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static br.com.ms.config.exception.enums.MensagensException.ENTIDADE_NAO_ENCONTRADA;

@Service
public class TransacaoServiceImpl implements TransacaoService{

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private PagamentoServiceUtils pagamentoServiceUtils;

    @Override
    public Transacao buscar(UUID id) {
        return this.transacaoRepository.findById(id)
                .orElseThrow(() -> new ServiceException(ENTIDADE_NAO_ENCONTRADA.getDescricao()));
    }

    @Override
    public List<Transacao> buscarTransacoes(UUID pagamentoId) {
        Pagamento pagamento = pagamentoServiceUtils.buscarPagamento(pagamentoId);

        return this.transacaoRepository.findByPagamento(pagamento);
    }

    @Override
    public Transacao cadastrar(TransacaoDto.Request.Transacao dto) {
        Pagamento pagamento = pagamentoServiceUtils.buscarPagamento(UUID.fromString(dto.getPagamentoId()));

        Transacao transacao = DtoService.dtoToEntity(dto, Transacao.class);
        transacao.setPagamento(pagamento);
        transacao.setStatus(dto.getStatus());
        transacao.setMensagem(dto.getStatus().getDescricao());

        return this.transacaoRepository.save(transacao);
    }
}
