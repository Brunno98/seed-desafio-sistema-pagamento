package br.com.brunno.yfood.application.pagamento;

import br.com.brunno.yfood.domain.entity.FormaPagamento;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class FormaPagamentoOnlinePedidoValidadorTest {

    public static final String MENSAGEM_DEFAULT_DO_VALIDOR = "forma de pagamento precisa ser online";

    NovoPedidoComPagamentoOnlineRequest request = new NovoPedidoComPagamentoOnlineRequest();
    Errors errors = new BeanPropertyBindingResult(null, "teste");
    FormaPagamentoOnlinePedidoValidador validador = new FormaPagamentoOnlinePedidoValidador();

    @Test
    @DisplayName("Quando já houver erro, então não valida")
    void alreadyHasError() {
        errors.reject(null, "Erro que ocorreu em outro validador");
        errors = Mockito.spy(errors);

        validador.validate(request, errors);

        verify(errors, never()).reject(null, MENSAGEM_DEFAULT_DO_VALIDOR);
    }

    @Test
    @DisplayName("Quando a forma de pagamento for online então é valido")
    void formaDePagamentoOnline() {
        ReflectionTestUtils.setField(request, "formaPagamento", FormaPagamento.elo);

        validador.validate(request, errors);

        Assertions.assertThat(errors.hasErrors()).isFalse();
    }

    @Test
    @DisplayName("Quando a forma de pagamento for offline então é invalido")
    void formaDePagamentoOffline() {
        errors = Mockito.spy(errors);
        ReflectionTestUtils.setField(request, "formaPagamento", FormaPagamento.dinheiro);

        validador.validate(request, errors);

        Assertions.assertThat(errors.hasErrors()).isTrue();
        verify(errors).reject(null, MENSAGEM_DEFAULT_DO_VALIDOR);
    }
}