package br.com.ms.transacao.repository;

import br.com.ms.pagamento.Pagamento;
import br.com.ms.transacao.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, UUID> {

    List<Transacao> findByPagamento(Pagamento pagamento);
}
