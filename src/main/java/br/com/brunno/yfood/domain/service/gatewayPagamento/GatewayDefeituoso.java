package br.com.brunno.yfood.domain.service.gatewayPagamento;

import br.com.brunno.yfood.domain.entity.FormaPagamento;
import br.com.brunno.yfood.domain.entity.GatewayPagamento;
import br.com.brunno.yfood.domain.entity.Pagamento;
import br.com.brunno.yfood.domain.entity.StatusTransacao;
import br.com.brunno.yfood.domain.entity.Transacao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Profile("dev")
@Service
public class GatewayDefeituoso implements GatewayPagamento {
    public static final Logger log = LoggerFactory.getLogger(Seya.class);

    @Override
    public Transacao processa(Pagamento pagamento) {
        log.info("Tentando tarifar no gateway defeituoso...");
        return new Transacao(StatusTransacao.falha);
    }

    @Override
    public boolean aceita(Pagamento pagamento) {
        return true;
    }

    @Override
    public BigDecimal custo(Pagamento pagamento) {
        return BigDecimal.ZERO;
    }
}
