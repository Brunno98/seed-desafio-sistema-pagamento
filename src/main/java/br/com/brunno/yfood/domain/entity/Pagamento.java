package br.com.brunno.yfood.domain.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated
    private FormaPagamento formaPagamento;

    @ManyToOne
    private Restaurante restaurante;

    private BigDecimal valor;

    @ManyToOne
    private Usuario usuario;

    private Long idPedido;

    private String informacoesExtras;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Transacao> transacoes = new ArrayList<>();

    @Deprecated
    public Pagamento() {}

    public Pagamento(FormaPagamento formaPagamento, Restaurante restaurante, Usuario usuario, Pedido pedido) {
        Assert.notNull(formaPagamento, "Transacao precisa ter uma forma de pagamento");
        Assert.notNull(restaurante, "Transacao precisa ter um restaurante");
        Assert.notNull(pedido, "Transacao precisa receber um pedido");
        Assert.notNull(pedido.getValor(), "O pedido da transacao precisa ter um valor");
        Assert.notNull(pedido.getId(), "O pedido da transacao precisa ter um id");
        Assert.notNull(usuario, "Transacao precisa ter um usuario");
        this.formaPagamento = formaPagamento;
        this.restaurante = restaurante;
        this.usuario = usuario;
        this.idPedido = pedido.getId();
        this.valor = pedido.getValor();
        this.transacoes.add(new Transacao(StatusTransacao.esperandoPagamento));
    }

    @Override
    public String toString() {
        return "Pagamento{" +
                "id=" + id +
                ", formaPagamento=" + formaPagamento +
                ", valor=" + valor +
                ", idPedido=" + idPedido +
                ", informacoesExtras=" + informacoesExtras +
                ", transacoes=" + transacoes +
                '}';
    }

    public void conclui() {
        Assert.notEmpty(this.transacoes, "Pagamento "+id+" precisa ter transacoes em aberto para concluir");
        this.transacoes.add(new Transacao(StatusTransacao.concluido));
    }

    public boolean isConcluido() {
        return this.transacoes.stream().anyMatch(Transacao::concluida);
    }

    public BigDecimal getValor() {
        return this.valor;
    }

    public void registraTarifacaoFalha(){
        this.transacoes.add(new Transacao(StatusTransacao.falha));
    }

    public FormaPagamento getFormaPagamento() {
        return this.formaPagamento;
    }

    public DadosCartao getDadosCartao() {
        try {
            return new ObjectMapper().readValue(this.informacoesExtras, DadosCartao.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void setDadosCartao(DadosCartao dadosCartao) {
        try {
            this.informacoesExtras = new ObjectMapper().writeValueAsString(dadosCartao);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void registraTransacao(Transacao transacao) {
        this.transacoes.add(transacao);
    }

    public void registraTransacoes(List<Transacao> transacoes) {
        this.transacoes.addAll(transacoes);
    }
}
