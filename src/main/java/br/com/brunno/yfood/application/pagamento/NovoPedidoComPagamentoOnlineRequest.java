package br.com.brunno.yfood.application.pagamento;

import br.com.brunno.yfood.domain.entity.DadosCartao;
import br.com.brunno.yfood.domain.entity.FormaPagamento;
import br.com.brunno.yfood.domain.entity.Pagamento;
import br.com.brunno.yfood.domain.entity.Pedido;
import br.com.brunno.yfood.domain.entity.Restaurante;
import br.com.brunno.yfood.domain.entity.Usuario;
import br.com.brunno.yfood.infrastructure.validators.Exists;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.CreditCardNumber;

public class NovoPedidoComPagamentoOnlineRequest implements PedidoPagamentoRequest{

    @NotNull
    private Long idPedido;

    @NotNull
    @Exists(domain = Usuario.class, domainField = "id")
    private Long idUsuario;

    @NotNull
    @Exists(domain = Restaurante.class, domainField = "id")
    private Long idRestaurante;

    @NotNull
    private FormaPagamento formaPagamento;

    @NotEmpty
    @CreditCardNumber
    private String numeroCartao;

    @NotEmpty
    private String codigoSeguranca;

    public Long getIdPedido() {
        return idPedido;
    }

    @Override
    public Long getIdRestaurante() {
        return idRestaurante;
    }

    @Override
    public Long getIdUsuario() {
        return idUsuario;
    }

    @Override
    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public String getCodigoSeguranca() {
        return codigoSeguranca;
    }

    public Pagamento toPagamento(EntityManager entityManager, Pedido pedido) {
        Restaurante restaurante = entityManager.find(Restaurante.class, idRestaurante);
        Usuario usuario = entityManager.find(Usuario.class, idUsuario);
        Pagamento pagamentoOnline = new Pagamento(formaPagamento, restaurante, usuario, pedido);
        pagamentoOnline.setDadosCartao(new DadosCartao(numeroCartao, codigoSeguranca));
        return pagamentoOnline;
    }
}
