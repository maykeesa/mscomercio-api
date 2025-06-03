package br.com.ms.transacao.dto;

import br.com.ms.transacao.enums.TransacaoStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

public class TransacaoDto {

    public enum Request{;

        @Data
        public static class Transacao{
            private String pagamentoId;
            private TransacaoStatus status;
        }

    }

    public enum Reponse{;

        @Data
        public static class Transacao{
            private UUID id;
            private UUID pagamentoId;
            private TransacaoStatus status;
            private String mensagem;
            private LocalDateTime dataCriacao;
        }

    }

}
