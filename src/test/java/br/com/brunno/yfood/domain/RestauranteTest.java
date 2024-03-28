package br.com.brunno.yfood.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

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
}
