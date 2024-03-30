package br.com.brunno.yfood.application.pagamento;

import br.com.brunno.yfood.domain.entity.FormaPagamento;
import br.com.brunno.yfood.domain.entity.Restaurante;
import br.com.brunno.yfood.domain.entity.Usuario;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class FormaPagamentoOfflinePedidoValidadorTest {

    EntityManager entityManager = Mockito.mock(EntityManager.class);
    FormaPagamentoOfflinePedidoValidador validator = new FormaPagamentoOfflinePedidoValidador();
    NovoPedidoComPagamentoOfflineRequest request = new NovoPedidoComPagamentoOfflineRequest();


    @DisplayName("Caso a forma de pagamento seja online, então rejeita")
    @Test
    void test1() {
        ReflectionTestUtils.setField(request, "formaPagamento", FormaPagamento.elo);
        Errors errors = new BeanPropertyBindingResult(request, "request");

        validator.validate(request, errors);

        assertThat(errors.hasErrors()).isTrue();
        assertThat(errors.getFieldError("formaPagamento")).isNotNull();
    }

    @DisplayName("Caso a forma de pagamento seja offline, então aceita")
    @Test
    void test2() {
        ReflectionTestUtils.setField(request, "formaPagamento", FormaPagamento.dinheiro);
        Errors errors = new BeanPropertyBindingResult(request, "request");

        validator.validate(request, errors);

        assertThat(errors.hasErrors()).isFalse();
    }

    @DisplayName("Caso ja tenha algum erro entao não faz validacao")
    @Test
    void alreadyHasError() {
        Errors errors = mock(Errors.class);
        doReturn(true).when(errors).hasErrors();

        validator.validate(request, errors);

        verify(errors, never()).reject(eq(null), any());
        verify(entityManager, never()).find(eq(Restaurante.class), any());
        verify(entityManager, never()).find(eq(Usuario.class), any());
    }
}