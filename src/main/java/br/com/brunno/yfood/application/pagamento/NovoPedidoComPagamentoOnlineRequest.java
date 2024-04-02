package br.com.brunno.yfood.application.pagamento;

import br.com.brunno.yfood.domain.entity.FormaPagamento;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class NovoPedidoComPagamentoOnlineRequest {

    @NotNull
    private Long idPedido;

    @NotNull
    private Long idUsuario;

    @NotNull
    private FormaPagamento formaPagamento;

    @NotEmpty
    private String numeroCartao;

    @NotEmpty
    private String codigoSeguranca;

    public Long getIdPedido() {
        return idPedido;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public String getCodigoSeguranca() {
        return codigoSeguranca;
    }
}
