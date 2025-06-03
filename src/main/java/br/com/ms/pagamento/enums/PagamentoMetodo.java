package br.com.ms.pagamento.enums;

public enum PagamentoMetodo {
    PIX("PIX"),
    CARTAO_CREDITO("CARTAO_CREDITO"),
    CARTAO_DEBITO("CARTAO_DEBITO"),
    BOLETO("BOLETO");

    private final String valor;

    PagamentoMetodo(String descricao){
        this.valor = descricao;
    }

    public String getValor(){
        return valor;
    }
}
