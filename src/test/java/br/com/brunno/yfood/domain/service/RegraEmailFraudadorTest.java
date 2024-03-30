package br.com.brunno.yfood.domain.service;

import br.com.brunno.yfood.domain.entity.FormaPagamento;
import br.com.brunno.yfood.domain.entity.Usuario;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RegraEmailFraudadorTest {

    RegraEmailFraudador regraEmailFraudador = new RegraEmailFraudador();

    @DisplayName("Verificacao sera positiva quando o email for fraudador e a forma de pagamento for online")
    @ParameterizedTest
    @CsvSource({"fraudador@email.com,elo,true","naoFraude@email.com,elo,false","fraudador@email.com,dinheiro,false"})
    void test(String email, FormaPagamento formaPagamento, boolean resultadoEsperado) {
        Usuario usuario = new Usuario(email, Arrays.asList(FormaPagamento.values()));

        boolean resultado = regraEmailFraudador.verifica(usuario, formaPagamento);

        Assertions.assertThat(resultado).isEqualTo(resultadoEsperado);
    }

}