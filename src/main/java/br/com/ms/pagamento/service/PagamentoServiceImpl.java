package br.com.ms.pagamento.service;

import br.com.ms.config.exception.exceptions.ServiceException;
import br.com.ms.email.dto.EmailDto;
import br.com.ms.email.service.EmailService;
import br.com.ms.pagamento.Pagamento;
import br.com.ms.pagamento.dto.PagamentoDto;
import br.com.ms.pagamento.repository.PagamentoRepository;
import br.com.ms.pagamento.service.utils.PagamentoServiceUtils;
import br.com.ms.utils.service.DtoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

import static br.com.ms.pagamento.enums.PagamentoMetodo.BOLETO;
import static br.com.ms.pagamento.enums.PagamentoMetodo.CARTAO_CREDITO;
import static br.com.ms.pagamento.enums.PagamentoMetodo.CARTAO_DEBITO;
import static br.com.ms.pagamento.enums.PagamentoMetodo.PIX;
import static br.com.ms.transacao.enums.TransacaoStatus.CANCELADO;
import static br.com.ms.transacao.enums.TransacaoStatus.CONCLUIDO;
import static br.com.ms.transacao.enums.TransacaoStatus.PENDENTE;
import static br.com.ms.transacao.enums.TransacaoStatus.PROCESSANDO;

@Slf4j
@Service
public class PagamentoServiceImpl implements PagamentoService{

    @Autowired
    private PagamentoRepository pagamentoRepository;
    @Autowired
    private PagamentoServiceUtils pagamentoServiceUtils;
    @Autowired
    private EmailService emailService;

    @Override
    public Object buscar(String id, String pagamentoId, Pageable pageable) {
        if(Objects.nonNull(id)){
            return DtoService.entityToDto(pagamentoServiceUtils
                    .buscarPagamento(UUID.fromString(id)), PagamentoDto.Response.Pagamento.class);
        }

        if(Objects.nonNull(pagamentoId)){
            return DtoService.entityToDto(pagamentoServiceUtils
                    .buscarPagamentoPorPedido(UUID.fromString(pagamentoId)), PagamentoDto.Response.Pagamento.class);
        }

        Page<Pagamento> pagamentos = this.pagamentoRepository.findAll(pageable);
        return new PageImpl<>(DtoService.entitysToDtos(pagamentos.getContent(), PagamentoDto.Response.Pagamento.class));
    }

    @Override
    public PagamentoDto.Response.Pagamento cadastrar(PagamentoDto.Request.Pagamento dto) {
        PagamentoDto.Response.Pagamento pagamento = new PagamentoDto.Response.Pagamento();

        if(dto.getMetodoPagamento().equals(PIX.getValor())){
            pagamento = this.pagamentoServiceUtils.pagamentoViaPix(dto);
        }

        if(dto.getMetodoPagamento().equals(BOLETO.getValor())){
            pagamento = this.pagamentoServiceUtils.pagamentoViaBoleto(dto);
        }

        if((dto.getMetodoPagamento().equals(CARTAO_CREDITO.getValor()))
                || (dto.getMetodoPagamento().equals(CARTAO_DEBITO.getValor()))){
            pagamento = this.pagamentoServiceUtils.processamentoPagamento(
                    this.pagamentoServiceUtils.pagamentoViaCartaoCreditoEDebito(dto));

            log.info("----------------------------------------------------");
            log.info("Email | Inicio envio de email");
            EmailDto.Request.Email email = this.pagamentoServiceUtils.gerarBodyEmail(pagamento);
            email.getTransacoes().forEach(transacao -> {
                log.info(transacao.getStatus().toString());
                email.setEmailPendente(false);
                email.setEmailProcessando(false);
                email.setEmailConcluido(false);
                email.setEmailCancelado(false);

                if(transacao.getStatus().equals(PENDENTE)){
                    email.setEmailPendente(true);
                }

                if(transacao.getStatus().equals(PROCESSANDO)){
                    email.setEmailProcessando(true);
                }

                if(transacao.getStatus().equals(CONCLUIDO)){
                    email.setEmailConcluido(true);
                }

                if(transacao.getStatus().equals(CANCELADO)){
                    email.setEmailCancelado(true);
                }

                this.emailService.enviarEmail(email);
            });
            log.info("Email | Fim envio de email");
            log.info("----------------------------------------------------");
        }

        return pagamento;
    }

    @Override
    public PagamentoDto.Response.Pagamento validarPagamento(PagamentoDto.Request.Validar dto) {
        if(!dto.getMetodoPagamento().equals(PIX.getValor()) || !dto.getMetodoPagamento().equals(BOLETO.getValor())){
            throw new ServiceException("Esse pagamento já foi validade.");
        }

        Pagamento pagamento = this.pagamentoServiceUtils.buscarPagamento(UUID.fromString(dto.getPagamentoId()));
        return this.pagamentoServiceUtils.processamentoPagamento(pagamento);
    }
}
