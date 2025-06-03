package br.com.ms.pagamento.service.utils;

import br.com.ms.email.dto.EmailDto;
import br.com.ms.pagamento.Pagamento;
import br.com.ms.pagamento.dto.PagamentoDto;
import br.com.ms.pagamento.repository.PagamentoRepository;
import br.com.ms.pedido.Pedido;
import br.com.ms.pedido.repository.PedidoRepository;
import br.com.ms.transacao.Transacao;
import br.com.ms.transacao.enums.TransacaoStatus;
import br.com.ms.utils.service.DtoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static br.com.ms.config.exception.enums.MensagensException.ENTIDADE_NAO_ENCONTRADA;
import static br.com.ms.pagamento.enums.PagamentoMetodo.BOLETO;
import static br.com.ms.pagamento.enums.PagamentoMetodo.CARTAO_CREDITO;
import static br.com.ms.pagamento.enums.PagamentoMetodo.CARTAO_DEBITO;
import static br.com.ms.pagamento.enums.PagamentoMetodo.PIX;
import static br.com.ms.transacao.enums.TransacaoStatus.CANCELADO;
import static br.com.ms.transacao.enums.TransacaoStatus.CONCLUIDO;
import static br.com.ms.transacao.enums.TransacaoStatus.PENDENTE;
import static br.com.ms.transacao.enums.TransacaoStatus.PROCESSANDO;

@Service
public class PagamentoServiceUtils {

    @Autowired
    private PagamentoRepository pagamentoRepository;
    @Autowired
    private PedidoRepository pedidoRepository;

    public Pagamento buscarPagamento(UUID id){
        return this.pagamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ENTIDADE_NAO_ENCONTRADA.getDescricao()));
    }

    public Pagamento buscarPagamentoPorPedido(UUID pedidoId){
        return this.pagamentoRepository.findByPedidoId(pedidoId)
                .orElseThrow(() -> new EntityNotFoundException(ENTIDADE_NAO_ENCONTRADA.getDescricao()));
    }

    public PagamentoDto.Response.Pagamento pagamentoViaPix(PagamentoDto.Request.Pagamento dto){
        Pagamento pagamento = this.gerarPagamentoETransacao(dto);
        PagamentoDto.Response.Pagamento response =
                DtoService.entityToDto(pagamento, PagamentoDto.Response.Pagamento.class);

        response.setPixDados(new PagamentoDto.Response.PixDados(gerarCodigoPix(dto)));

        return response;
    }

    public Pagamento pagamentoViaCartaoCreditoEDebito(PagamentoDto.Request.Pagamento dto){
        return this.gerarPagamentoETransacao(dto);
    }

    public PagamentoDto.Response.Pagamento pagamentoViaBoleto(PagamentoDto.Request.Pagamento dto){
        Pagamento pagamento = this.gerarPagamentoETransacao(dto);
        PagamentoDto.Response.Pagamento response =
                DtoService.entityToDto(pagamento, PagamentoDto.Response.Pagamento.class);

        response.setBoletoDados(new PagamentoDto.Response.BoletoDados(gerarCodigoBarrasFake()));

        return response;
    }

    public PagamentoDto.Response.Pagamento processamentoPagamento(Pagamento pagamento){
        List<Transacao> transacoes = gerarTransacoesComplementares(pagamento);
        List<Transacao> transacoesPagamento = new ArrayList<>(pagamento.getTransacoes());
        transacoesPagamento.addAll(transacoes);
        pagamento.setTransacoes(transacoesPagamento);
        pagamento.setUltimoStatus(transacoes.get(transacoes.size() - 1).getStatus());

        Pagamento pagamentoAtualizado = this.pagamentoRepository.save(pagamento);
        return DtoService.entityToDto(pagamentoAtualizado, PagamentoDto.Response.Pagamento.class);
    }

    private List<Transacao> gerarTransacoesComplementares(Pagamento pagamento){
        List<Transacao> transacoes = new ArrayList<>();
        transacoes.add(obterTransacao(pagamento, PROCESSANDO));

        int chances = new Random().nextInt(10);
        if((pagamento.getMetodoPagamento().equals(CARTAO_CREDITO)
                || pagamento.getMetodoPagamento().equals(CARTAO_DEBITO)) && chances <= 2){
            transacoes.add(obterTransacao(pagamento, CANCELADO));
            pagamento.setDataCancelamento(LocalDateTime.now());
            return transacoes;
        }

        if(pagamento.getMetodoPagamento().equals(PIX) && chances <= 1){
            transacoes.add(obterTransacao(pagamento, CANCELADO));
            pagamento.setDataCancelamento(LocalDateTime.now());
            return transacoes;
        }

        if(pagamento.getMetodoPagamento().equals(BOLETO) && chances <= 4){
            transacoes.add(obterTransacao(pagamento, CANCELADO));
            pagamento.setDataCancelamento(LocalDateTime.now());
            return transacoes;
        }

        transacoes.add(obterTransacao(pagamento, CONCLUIDO));
        pagamento.setDataPagamento(LocalDateTime.now());
        return transacoes;
    }

    public EmailDto.Request.Email gerarBodyEmail(PagamentoDto.Response.Pagamento pagamento){
        EmailDto.Request.Email email = new EmailDto.Request.Email();
        email.setPedidoId(pagamento.getPedido().getId());
        email.setNome(pagamento.getPedido().getConta().getNome());
        email.setValorPedido(pagamento.getPedido().getValorTotal());
        email.setDataPagamento(pagamento.getDataPagamento());
        email.setDataCancelamento(pagamento.getDataCancelamento());
        email.setDestinatario(pagamento.getPedido().getConta().getEmail());
        email.setTransacoes(pagamento.getTransacoes());

        return email;
    }

    private String gerarCodigoPix(PagamentoDto.Request.Pagamento pagamento){
        Pedido pedido = pedidoRepository.findById(UUID.fromString(pagamento.getPedidoId()))
                .orElseThrow(() -> new EntityNotFoundException(ENTIDADE_NAO_ENCONTRADA.getDescricao()));

        String chaveAleatoria = UUID.randomUUID().toString().replace("-", "").substring(0, 25)
                .toUpperCase();

        return "00020126430014BR.GOV.BCB.PIX0114" + chaveAleatoria + "5204000053039865802BR5913 "
                + pedido.getConta().getNome().toUpperCase().trim() + " MSCOMERCEIOR600962070503.79";
    }

    private String gerarCodigoBarrasFake() {
        Random random = new Random();
        StringBuilder base = new StringBuilder();

        for (int i = 0; i < 12; i++) {
            base.append(random.nextInt(10));
        }

        int digito = calcularDigitoVerificador(base.toString());
        base.append(digito);

        return base.toString();
    }

    private int calcularDigitoVerificador(String codigo) {
        int soma = 0;
        for (int i = 0; i < codigo.length(); i++) {
            int digito = Character.getNumericValue(codigo.charAt(i));
            soma += (i % 2 == 0) ? digito : digito * 3;
        }

        int mod = soma % 10;
        return (mod == 0) ? 0 : 10 - mod;
    }

    private Pagamento gerarPagamentoETransacao(PagamentoDto.Request.Pagamento dto){
        Pedido pedido = this.pedidoRepository.findById(UUID.fromString(dto.getPedidoId()))
                .orElseThrow(() -> new EntityNotFoundException(ENTIDADE_NAO_ENCONTRADA.getDescricao()));

        Pagamento pagamento = DtoService.dtoToEntity(dto, Pagamento.class);
        pagamento.setId(null);

        pagamento.setPedido(pedido);
        pagamento.setUltimoStatus(PENDENTE);
        pagamento.setTransacoes(List.of(obterTransacao(pagamento, PENDENTE)));
        return this.pagamentoRepository.saveAndFlush(pagamento);
    }

    private Transacao obterTransacao(Pagamento pagamento, TransacaoStatus status){
        Transacao transacao = new Transacao();
        transacao.setPagamento(pagamento);
        transacao.setStatus(status);
        transacao.setMensagem(status.getDescricao());

        return transacao;
    }
}
