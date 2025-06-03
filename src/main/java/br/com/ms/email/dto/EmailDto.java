package br.com.ms.email.dto;

import br.com.ms.transacao.dto.TransacaoDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static br.com.ms.utils.constants.MensagensConstants.EMAIL;
import static br.com.ms.utils.constants.MensagensConstants.NAO_NULO;
import static br.com.ms.utils.constants.MensagensConstants.NAO_NULO_BRANCO;

public class EmailDto {

    public enum Request{;

        @Data
        public static class Email{

            @NotNull(message = NAO_NULO)
            private UUID pedidoId;
            @NotBlank(message = NAO_NULO_BRANCO)
            private String nome;
            @NotNull(message = NAO_NULO)
            private BigDecimal valorPedido;
            private LocalDateTime dataPagamento;
            private LocalDateTime dataCancelamento;
            @jakarta.validation.constraints.Email(message = EMAIL)
            @NotBlank(message = NAO_NULO_BRANCO)
            private String destinatario;
            private boolean emailPendente = false;
            private boolean emailProcessando = false;
            private boolean emailConcluido = false;
            private boolean emailCancelado = false;
            private List<TransacaoDto.Reponse.Transacao> transacoes;
        }

    }

    public enum Response{;

        @Data
        public static class Email{
            private String destinatario;
            private String status;
            private String descricao;
        }

    }

}
