package br.com.brunno.yfood.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

public class UsuarioTest {


    @DisplayName("Usuario precisa ter pelo menos uma forma de pagamento")
    @Test
    void test() {
        Assertions
                .assertThatIllegalArgumentException()
                .isThrownBy(() -> new Usuario("teste", Collections.emptyList()));

        Assertions
                .assertThatCode(() -> new Usuario("teste", List.of(FormaPagamento.dinheiro)))
                .doesNotThrowAnyException();
    }

}
