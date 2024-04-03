package br.com.brunno.yfood.domain.service.gatewayPagamento;

import br.com.brunno.yfood.domain.entity.GatewayPagamento;
import br.com.brunno.yfood.domain.entity.Pagamento;
import br.com.brunno.yfood.domain.entity.Transacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class Gateways {

    private final List<GatewayPagamento> gateways;

    @Autowired
    public Gateways(List<GatewayPagamento> gateways) {
        this.gateways = gateways;
    }

    public List<Transacao> processa(Pagamento pagamento) {
        List<GatewayPagamento> gateways = this.gateways.stream()
                .filter(gateway -> gateway.aceita(pagamento))
                .sorted(Comparator.comparing(gateway -> gateway.custo(pagamento)))
                .toList();

        List<Transacao> transacoes = new ArrayList<>();
        for (GatewayPagamento gateway : gateways) {
            Transacao transacao = gateway.processa(pagamento);
            transacoes.add(transacao);
            if (transacao.concluida()) {
                break;
            }
        }
        return transacoes;
    }
}
