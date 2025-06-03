package br.com.ms.transacao.enums;

public enum TransacaoStatus {

    PENDENTE("Aguardando processamento."),
    PROCESSANDO("Transação está sendo processada."),
    CONCLUIDO("Transação concluída com sucesso."),
    CANCELADO("Transação cancelada pelo sistema.");

    private final String descricao;

    TransacaoStatus(String descricao){
        this.descricao = descricao;
    }

    public String getDescricao(){
        return descricao;
    }
}
