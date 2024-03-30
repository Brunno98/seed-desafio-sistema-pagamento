package br.com.brunno.yfood.application.pagamento;

import br.com.brunno.yfood.domain.entity.FormaPagamento;
import br.com.brunno.yfood.domain.entity.Pedido;
import br.com.brunno.yfood.domain.entity.Restaurante;
import br.com.brunno.yfood.domain.entity.Transacao;
import br.com.brunno.yfood.domain.entity.Usuario;
import br.com.brunno.yfood.infrastructure.validators.Exists;
import jakarta.persistence.EntityManager;
import jakarta.validation.constraints.NotNull;

public class NovoPedidoComPagamentoOfflineRequest {

    @NotNull
    private FormaPagamento formaPagamento;

    @NotNull
    @Exists(domainField = "id", domain = Restaurante.class)
    private Long idRestaurante;

    @NotNull
    private Long idPedido;

    @NotNull
    @Exists(domainField = "id", domain = Usuario.class)
    private Long idUsuario;

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public Long getIdRestaurante() {
        return idRestaurante;
    }

    public Long getIdPedido() {
        return idPedido;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public Transacao toTransacao(EntityManager entityManager, Pedido pedido) {
        Restaurante restaurante = entityManager.find(Restaurante.class, idRestaurante);
        Usuario usuario = entityManager.find(Usuario.class, idUsuario);

        return new Transacao(formaPagamento, restaurante, usuario, pedido);
    }
}
