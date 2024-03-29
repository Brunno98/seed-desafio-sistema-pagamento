package br.com.brunno.yfood.domain.entity;

public enum FormaPagamento {
    dinheiro(false, "Pagamento em dinheiro na entrega do pedido"),
    maquina(false, "Pagamento com máquina de cartão na entrega do pedido"),
    cheque(false, "Pagamento em cheque"),
    elo(true, "Pagamento com cartão elo"),
    hipercard(true, "Pagamento com cartão hipercard"),
    mastercard(true, "Pagamento com cartão mastercard");


    public final boolean online;
    public final String descricao;

    FormaPagamento(boolean online, String descricao) {
        this.online = online;
        this.descricao = descricao;
    }
}
