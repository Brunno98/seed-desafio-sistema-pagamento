package br.com.brunno.yfood.application.pagamento;

import br.com.brunno.yfood.domain.entity.FormaPagamento;

public interface PedidoPagamentoRequest {
    Long getIdRestaurante();

    Long getIdUsuario();

    FormaPagamento getFormaPagamento();
}
