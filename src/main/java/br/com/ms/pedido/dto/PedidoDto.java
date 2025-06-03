package br.com.ms.pedido.dto;

import br.com.ms.conta.dto.ContaDto;
import br.com.ms.endereco.Endereco;
import br.com.ms.endereco.dto.EnderecoDto;
import br.com.ms.pedido.enums.PedidoStatus;
import br.com.ms.produto.dto.ProdutoDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static br.com.ms.utils.constants.MensagensConstants.EMAIL;
import static br.com.ms.utils.constants.MensagensConstants.METODO_PAGAMENTO;
import static br.com.ms.utils.constants.MensagensConstants.NAO_NULO_BRANCO;

public class PedidoDto {

    public enum Request{;

        @Data
        public static class Pedido {
            @NotBlank(message = NAO_NULO_BRANCO)
            private String contaId;
            @Pattern(regexp = "PIX|CARTAO_CREDITO|CARTAO_DEBITO|BOLETO",
                    message = METODO_PAGAMENTO)
            @NotNull(message = NAO_NULO_BRANCO)
            private String metodoPagamento;
            @NotNull(message = "A lista de produtos não pode ser nula.")
            private List<UUID> produtosIds;
        }
    }

    public enum Response{;

        @Data
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Pedido {
            private UUID id;
            private String nrPedido;
            private ContaDto.Response.Conta conta;
            private EnderecoDto.Response.Endereco endereco;
            private List<ProdutoDto.Response.Produto> produtos;
            private String metodoPagamento;
            private BigDecimal valorTotal;
            private PedidoStatus status;
            private LocalDateTime dataCriacao;
            private LocalDateTime dataAtualizacao;
        }
    }
}
