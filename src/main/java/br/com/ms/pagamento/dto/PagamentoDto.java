package br.com.ms.pagamento.dto;

import br.com.ms.pagamento.enums.PagamentoMetodo;
import br.com.ms.pedido.dto.PedidoDto;
import br.com.ms.transacao.dto.TransacaoDto;
import br.com.ms.transacao.enums.TransacaoStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static br.com.ms.utils.constants.MensagensConstants.EMAIL;
import static br.com.ms.utils.constants.MensagensConstants.METODO_PAGAMENTO;
import static br.com.ms.utils.constants.MensagensConstants.NAO_NULO;
import static br.com.ms.utils.constants.MensagensConstants.NAO_NULO_BRANCO;

public class PagamentoDto {

    public enum Request{;

        @Data
        public static class CartaoDados{
            private String nomeCartao;
            private String numeroCartao;
            private String dataValidadeCartao;
            private String CVV;
        }

        @Data
        public static class Pagamento{
            @NotBlank(message = NAO_NULO_BRANCO)
            private String pedidoId;
            @Pattern(regexp = "PIX|CARTAO_CREDITO|CARTAO_DEBITO|BOLETO",
                    message = METODO_PAGAMENTO)
            @NotNull(message = NAO_NULO_BRANCO)
            private String metodoPagamento;
        }

        @Data
        public static class Validar{
            @NotNull(message = NAO_NULO_BRANCO)
            private String nome;
            @Size(min = 11, max = 11, message = "O valor tem que ter 11 caractéres.")
            @NotBlank(message = NAO_NULO_BRANCO)
            private String cpf;
            @Email(message = EMAIL)
            @NotNull(message = NAO_NULO_BRANCO)
            private String email;

            @NotBlank(message = NAO_NULO_BRANCO)
            private String pagamentoId;
            @Pattern(regexp = "PIX|CARTAO_CREDITO|CARTAO_DEBITO|BOLETO",
                    message = METODO_PAGAMENTO)
            @NotNull(message = NAO_NULO_BRANCO)
            private String metodoPagamento;
        }

    }

    public enum Response {;

        @Data
        @AllArgsConstructor
        public static class PixDados{
            private String codigoPix;
        }

        @Data
        @AllArgsConstructor
        public static class BoletoDados{
            private String codigoDeBarra;
        }

        @Data
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Pagamento{
            private UUID id;
            private PedidoDto.Response.Pedido pedido;
            private TransacaoStatus ultimoStatus;
            private PagamentoMetodo metodoPagamento;
            private PixDados pixDados;
            private BoletoDados boletoDados;
            private List<TransacaoDto.Reponse.Transacao> transacoes;
            private LocalDateTime dataPagamento;
            private LocalDateTime dataCancelamento;
            private LocalDateTime dataCriacao;
        }

    }
}

