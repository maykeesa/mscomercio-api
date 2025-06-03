package br.com.ms.transacao;

import br.com.ms.pagamento.Pagamento;
import br.com.ms.transacao.enums.TransacaoStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "transacoes")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Transacao {

    @Id
    @EqualsAndHashCode.Include
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pagamento_id", nullable = false)
    private Pagamento pagamento;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransacaoStatus status;

    @Column(nullable = false)
    private String mensagem;

    @CreationTimestamp
    private LocalDateTime dataCriacao;

    @Override
    public String toString() {
        return "Transacao{" +
                "id=" + id +
                ", pagamento=" + pagamento.getId() +
                ", status=" + status +
                ", mensagem='" + mensagem + '\'' +
                ", dataCriacao=" + dataCriacao +
                '}';
    }
}
