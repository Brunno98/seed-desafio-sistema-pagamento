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
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class Seya implements GatewayPagamento {
    public static final Logger log = LoggerFactory.getLogger(Seya.class);
    private static final List<FormaPagamento> formasDePagamentoAceitas = List.of(
            FormaPagamento.elo, FormaPagamento.mastercard, FormaPagamento.hipercard, FormaPagamento.visa);

    @Override
    public Transacao processa(Pagamento pagamento) {
        log.info("Usando o Gateway de Pagamento Seya");
        log.info("Valor da compra: " + pagamento.getValor());
        DadosCartao dadosCartao = pagamento.getDadosCartao();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<CartaoValidoResponse> cartaoValidoResponseResponseEntity =
                restTemplate.postForEntity("http://localhost:8080/validar", Map.of("num_cartao", dadosCartao.getNumeroCartao(),
                "codigo_seguranca", dadosCartao.getCodigoSeguranca()), CartaoValidoResponse.class);

        if (!cartaoValidoResponseResponseEntity.getStatusCode().is2xxSuccessful()) return new Transacao(StatusTransacao.falha);

        CartaoValidoResponse cartaoValidoResponse = cartaoValidoResponseResponseEntity.getBody();
        Assert.notNull(cartaoValidoResponse, "Resposta de validacao de cartao do gateway Seya não deveria ser nulo.");

        ResponseEntity<Void> response = restTemplate.postForEntity("http://localhost:8080/tarifa/" + cartaoValidoResponse.idProcessamento, Map.of(
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
        // Custo fixo de R$6,00
        BigDecimal valorTaxa = BigDecimal.valueOf(6);
        log.info("[SEYA] Valor da taxa: " + valorTaxa.toString());
        return valorTaxa;
    }

    private record CartaoValidoResponse(String idProcessamento) {};
}
