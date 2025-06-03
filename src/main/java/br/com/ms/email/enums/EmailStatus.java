package br.com.ms.email.enums;

public enum EmailStatus {

    ENVIADO("Enviado", "Email enviado com sucesso."),
    NAO_ENVIADO("Não enviado", "Não foi possível enviar o email no momento.");

    private final String mensagem;
    private final String descricao;

    EmailStatus(String mensagem, String descricao){
        this.mensagem = mensagem;
        this.descricao = descricao;
    }

    public String getDescricao(){
        return descricao;
    }

    public String getMensagem(){
        return mensagem;
    }
}
