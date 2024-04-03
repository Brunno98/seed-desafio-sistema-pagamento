package br.com.brunno.yfood.domain.service.gatewayPagamento;

import br.com.brunno.yfood.domain.entity.DadosCartao;
import br.com.brunno.yfood.domain.entity.FormaPagamento;
import br.com.brunno.yfood.domain.entity.GatewayPagamento;
import br.com.brunno.yfood.domain.entity.Pagamento;
import br.com.brunno.yfood.domain.entity.StatusTransacao;
import br.com.brunno.yfood.domain.entity.Transacao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class Saori implements GatewayPagamento {
    public static final Logger log = LoggerFactory.getLogger(Saori.class);
    public static final List<FormaPagamento> formasDePagamentoAceitas = List.of(
            FormaPagamento.mastercard, FormaPagamento.visa
    );

    @Override
    public Transacao processa(Pagamento pagamento) {
        log.info("Usando o Gateway de Pagamento Saori");
        log.info("Valor da compra: " + pagamento.getValor().toString());
        DadosCartao dadosCartao = pagamento.getDadosCartao();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Void> response = restTemplate.postForEntity("http://localhost:8080/tarifa/" + UUID.randomUUID().toString(), Map.of(
                "num_cartao", dadosCartao.getNumeroCartao(),
                "codigo_seguranca", dadosCartao.getCodigoSeguranca(),
                "valor_compra", pagamento.getValor()
        ), Void.class);
        StatusTransacao statusTransacao = response.getStatusCode().is2xxSuccessful()?
                StatusTransacao.concluido:
                StatusTransacao.falha;
        return new Transacao(statusTransacao);
    }

    @Override
    public boolean aceita(Pagamento pagamento) {
        return formasDePagamentoAceitas.contains(pagamento.getFormaPagamento());
    }

    @Override
    public BigDecimal custo(Pagamento pagamento) {
        BigDecimal valor = pagamento.getValor();
        BigDecimal valorTaxa = valor.multiply(BigDecimal.valueOf(0.05));
        log.info("[SAORI] valor taxa: " + valorTaxa.toString());
        return valorTaxa;
    }
}
