package br.com.brunno.yfood.domain.service.gatewayPagamento;

import br.com.brunno.yfood.domain.entity.FormaPagamento;
import br.com.brunno.yfood.domain.entity.Pagamento;
import br.com.brunno.yfood.domain.entity.Pedido;
import br.com.brunno.yfood.domain.entity.Restaurante;
import br.com.brunno.yfood.domain.entity.Usuario;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SaoriTest {

    Restaurante restaurante = new Restaurante("teste restaurante", List.of(FormaPagamento.elo));
    Usuario usuario = new Usuario("teste@email.com", List.of(FormaPagamento.elo));
    Saori saori = new Saori();

    @DisplayName("Deve calcular o custo de 6% do valor do pagamento")
    @ParameterizedTest
    @CsvSource({"100,5.00", "50,2.50", "42,2.10"})
    void test(BigDecimal valorPagamento, BigDecimal custoEsperado) {
        Pedido pedido = new Pedido() {
            @Override public Long getId() { return 1L; }
            @Override public BigDecimal getValor() { return valorPagamento; }
        };
        Pagamento pagamento = new Pagamento(FormaPagamento.mastercard, restaurante, usuario, pedido);

        BigDecimal custoCalculado = saori.custo(pagamento);

        Assertions.assertThat(custoCalculado).isEqualTo(custoEsperado);
    }

}