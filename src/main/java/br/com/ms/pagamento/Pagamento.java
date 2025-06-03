package br.com.ms.pagamento;

import br.com.ms.pagamento.enums.PagamentoMetodo;
import br.com.ms.pedido.Pedido;
import br.com.ms.transacao.Transacao;
import br.com.ms.transacao.dto.TransacaoDto;
import br.com.ms.transacao.enums.TransacaoStatus;
import br.com.ms.utils.service.DtoService;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "pagamentos")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pagamento {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "pedido_id", nullable = false, unique = true)
    private Pedido pedido;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransacaoStatus ultimoStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PagamentoMetodo metodoPagamento;

    @OneToMany(mappedBy = "pagamento", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transacao> transacoes;

    private LocalDateTime dataPagamento;

    private LocalDateTime dataCancelamento;

    @CreationTimestamp
    private LocalDateTime dataCriacao;

    @Override
    public String toString() {
        return "Pagamento{" +
                "id=" + id +
                ", pedidoId=" + pedido.getId() +
                ", ultimoStatus=" + ultimoStatus +
                ", metodoPagamento=" + metodoPagamento +
                ", transacoes=" + DtoService.entitysToDtos(transacoes, TransacaoDto.Reponse.Transacao.class) +
                ", dataPagamento=" + dataPagamento +
                ", dataCriacao=" + dataCriacao +
                '}';
    }
}
