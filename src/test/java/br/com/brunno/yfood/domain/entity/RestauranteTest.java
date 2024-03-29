package br.com.brunno.yfood.domain.entity;

import br.com.brunno.yfood.domain.entity.FormaPagamento;
import br.com.brunno.yfood.domain.entity.Restaurante;
import br.com.brunno.yfood.domain.service.RegraFraude;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

public class RestauranteTest {

    @DisplayName("Restaurante precisa ter no minimo 1 forma de pagamento")
    @Test
    void test() {
        Assertions
                .assertThatIllegalArgumentException()
                .isThrownBy(() -> new Restaurante("teste", Collections.emptyList()));

        Assertions
                .assertThatCode(() -> new Restaurante("teste", List.of(FormaPagamento.dinheiro)))
                .doesNotThrowAnyException();
    }

    @DisplayName("Quando uma regra de fraude se aplicar ao usuario e forma de pagamento, então esta forma de pagamento não deve estar disponivel")
    @Test
    void test2() {
        Usuario valido = new Usuario("valido@email.com", List.of(FormaPagamento.dinheiro, FormaPagamento.elo, FormaPagamento.mastercard));
        Usuario fraudador = new Usuario("fraudador@email.com", List.of(FormaPagamento.dinheiro, FormaPagamento.elo, FormaPagamento.mastercard));
        Restaurante restaurante = new Restaurante("restaurante foo", List.of(FormaPagamento.dinheiro, FormaPagamento.elo, FormaPagamento.mastercard, FormaPagamento.hipercard));
        RegraFraude regraFraudadorSoPodePagarEmDinheiro = (Usuario usuario, FormaPagamento formaPagamento) ->
                usuario.getEmail().equals("fraudador@email.com") && !FormaPagamento.dinheiro.equals(formaPagamento);

        List<FormaPagamento> formasPagamentoParaUsuarioValido = restaurante.formasDePagamentosAceitasParaUsuario(valido, List.of(regraFraudadorSoPodePagarEmDinheiro));
        List<FormaPagamento> formasPagamentoParaUsuarioFraudador = restaurante.formasDePagamentosAceitasParaUsuario(fraudador, List.of(regraFraudadorSoPodePagarEmDinheiro));

        assertThat(formasPagamentoParaUsuarioValido).hasSize(3);
        assertThat(formasPagamentoParaUsuarioFraudador).hasSize(1);
        assertThat(formasPagamentoParaUsuarioFraudador).contains(FormaPagamento.dinheiro);
    }
}
