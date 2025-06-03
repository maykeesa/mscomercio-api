package br.com.ms.produto;

import br.com.ms.pedido.Pedido;
import br.com.ms.produto.enums.DisponibilidadeStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String categoria;

    @Column(nullable = false)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal preco;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DisponibilidadeStatus disponibilidade;

    @Column(nullable = false)
    @Positive(message = "A quantidade deve ser positiva")
    private Integer estoque;

    @CreationTimestamp
    private LocalDateTime dataCriacao;

    private LocalDateTime dataAtualizacao;

    @ManyToMany(mappedBy = "produtos", fetch = FetchType.LAZY)
    private List<Pedido> pedidos;

}

