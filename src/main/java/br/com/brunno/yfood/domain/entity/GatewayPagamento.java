package br.com.brunno.yfood.domain.entity;

import java.math.BigDecimal;

public interface GatewayPagamento {

    Transacao processa(Pagamento pagamento);

    boolean aceita(Pagamento pagamento);

    BigDecimal custo(Pagamento pagamento);

}
