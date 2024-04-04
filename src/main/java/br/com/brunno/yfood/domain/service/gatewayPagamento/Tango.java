package br.com.brunno.yfood.domain.service.gatewayPagamento;

import br.com.brunno.yfood.domain.entity.DadosCartao;
import br.com.brunno.yfood.domain.entity.FormaPagamento;
import br.com.brunno.yfood.domain.entity.GatewayPagamento;
import br.com.brunno.yfood.domain.entity.Pagamento;
import br.com.brunno.yfood.domain.entity.StatusTransacao;
import br.com.brunno.yfood.domain.entity.Transacao;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

@Service
public class Tango implements GatewayPagamento {
    @Override
    public Transacao processa(Pagamento pagamento) {
        DadosCartao dadosCartao = pagamento.getDadosCartao();
        ResponseEntity<Void> response = new RestTemplate().postForEntity("http://localhost:8080/tarifa", Map.of(
                "numero_cartao", dadosCartao.getNumeroCartao(),
                "codigo_seguranca", dadosCartao.getCodigoSeguranca(),
                "valor_compra", pagamento.getValor().toString()
        ), Void.class);

        StatusTransacao statusTransacao = response.getStatusCode().is2xxSuccessful()?
                StatusTransacao.concluido : StatusTransacao.falha;
        return new Transacao(statusTransacao);
    }

    @Override
    public boolean aceita(Pagamento pagamento) {
        return Arrays.stream(FormaPagamento.values())
                .filter(formaPagamento -> formaPagamento.online)
                .anyMatch(formaPagamento -> formaPagamento.equals(pagamento.getFormaPagamento()));
    }

    @Override
    public BigDecimal custo(Pagamento pagamento) {
        BigDecimal valor = pagamento.getValor();
        if (valor.compareTo(BigDecimal.valueOf(100)) <= 0) {
            return BigDecimal.valueOf(4);
        }
        return valor.multiply(BigDecimal.valueOf(0.06));
    }
}
