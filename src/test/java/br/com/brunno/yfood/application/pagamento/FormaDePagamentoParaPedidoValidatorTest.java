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

import java.util.Collections;
import java.util.List;

import static br.com.brunno.yfood.domain.entity.FormaPagamento.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FormaDePagamentoParaPedidoValidatorTest {

    EntityManager entityManager = mock(EntityManager.class);
    FormaDePagamentoParaPedidoValidator validator =
            new FormaDePagamentoParaPedidoValidator(entityManager, Collections.emptyList());
    NovoPedidoComPagamentoOfflineRequest request = new NovoPedidoComPagamentoOfflineRequest();

    @DisplayName("Caso a forma de pagamento não esteja disponivel para o restaurante ou usuario, então deve rejeitar")
    @Test
    void test() {
        Usuario usuario = new Usuario("teste@email.com", List.of(dinheiro, maquina, elo, hipercard, mastercard));
        Restaurante restaurante = new Restaurante("Restaurante teste", List.of(dinheiro, maquina, elo, hipercard, mastercard));
        doReturn(usuario).when(entityManager).find(Usuario.class, 1L);
        doReturn(restaurante).when(entityManager).find(Restaurante.class, 1L);
        ReflectionTestUtils.setField(request, "idUsuario", 1L);
        ReflectionTestUtils.setField(request, "idRestaurante", 1L);
        ReflectionTestUtils.setField(request, "formaPagamento", cheque);
        Errors errors = new BeanPropertyBindingResult(request, "request");

        validator.validate(request, errors);

        assertThat(errors.hasErrors()).isTrue();
        assertThat(errors.getGlobalErrorCount()).isEqualTo(1);
    }

    @DisplayName("Caso a forma de pagamento esteja disponivel para usuario e restaurante, então é valido")
    @Test
    void test2() {
        Usuario usuario = new Usuario("teste@email.com", List.of(dinheiro, maquina, elo, hipercard, mastercard));
        Restaurante restaurante = new Restaurante("Restaurante teste", List.of(dinheiro, maquina, elo, hipercard, mastercard));
        doReturn(usuario).when(entityManager).find(Usuario.class, 1L);
        doReturn(restaurante).when(entityManager).find(Restaurante.class, 1L);
        ReflectionTestUtils.setField(request, "idUsuario", 1L);
        ReflectionTestUtils.setField(request, "idRestaurante", 1L);
        ReflectionTestUtils.setField(request, "formaPagamento", dinheiro);
        Errors errors = new BeanPropertyBindingResult(request, "request");

        validator.validate(request, errors);

        assertThat(errors.hasErrors()).isFalse();
        verify(entityManager).find(Restaurante.class, 1L);
        verify(entityManager).find(Usuario.class, 1L);
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