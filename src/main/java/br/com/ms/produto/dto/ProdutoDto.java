package br.com.ms.produto.dto;

import br.com.ms.produto.enums.DisponibilidadeStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static br.com.ms.utils.constants.MensagensConstants.DISPONIBILIDADE;
import static br.com.ms.utils.constants.MensagensConstants.NAO_NULO;
import static br.com.ms.utils.constants.MensagensConstants.NAO_NULO_BRANCO;
import static br.com.ms.utils.constants.MensagensConstants.POSITIVO;

public class ProdutoDto {

    public enum Request{;

        @Data
        public static class Produto{
            @NotBlank(message = NAO_NULO_BRANCO)
            private String nome;
            @NotBlank(message = NAO_NULO_BRANCO)
            private String categoria;
            @NotBlank(message = NAO_NULO_BRANCO)
            private String descricao;
            @NotNull(message = NAO_NULO)
            private BigDecimal preco;
            @Pattern(regexp = "DISPONIVEL|INDISPONIVEL",
                    message = DISPONIBILIDADE)
            @NotNull(message = NAO_NULO)
            private String disponibilidade;
            @Positive(message = POSITIVO)
            @NotNull(message = NAO_NULO)
            private Integer estoque;
        }
    }

    public enum Response{;

        @Data
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Produto{
            private UUID id;
            private String nome;
            private String categoria;
            private String descricao;
            private BigDecimal preco;
            private DisponibilidadeStatus disponibilidade;
            private Integer estoque;
            private LocalDateTime dataCriacao;
            private LocalDateTime dataAtualizacao;
        }
    }

}
