package br.com.brunno.yfood.domain.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Transacao {

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

    @Enumerated(EnumType.STRING)
    private StatusTransacao status = StatusTransacao.esperandoPagamento;

    private LocalDateTime instanteDeCriacao = LocalDateTime.now();

    @ElementCollection
    private List<String> informacoesExtras = new ArrayList<>();

    @Deprecated
    public Transacao() {}

    public Transacao(FormaPagamento formaPagamento, Restaurante restaurante, Usuario usuario, Pedido pedido) {
        Assert.notNull(formaPagamento, "Transacao precisa ter uma forma de pagamento");
        Assert.notNull(restaurante, "Transacao precisa ter um restaurante");
        Assert.notNull(pedido, "Transacao precisa receber um pedido");
        Assert.notNull(pedido.getValor(), "O pedido da transacao precisa ter um valor");
        Assert.notNull(pedido.getId(), "O pedido da transacao precisa ter um id");
        Assert.notNull(usuario, "Transacao precisa ter um usuario");
        this.formaPagamento = formaPagamento;
        this.restaurante = restaurante;
        this.valor = pedido.getValor();
        this.usuario = usuario;
        this.idPedido = pedido.getId();
    }

    @Override
    public String toString() {
        return "Transacao{" +
                "formaPagamento=" + formaPagamento +
                ", restaurante=" + restaurante +
                ", valor=" + valor +
                ", usuario=" + usuario +
                ", idPedido=" + idPedido +
                ", status=" + status +
                ", instanteDeCriacao=" + instanteDeCriacao +
                '}';
    }
}
