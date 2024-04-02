package br.com.brunno.yfood.application.pagamento;

import br.com.brunno.yfood.domain.entity.FormaPagamento;
import br.com.brunno.yfood.domain.entity.Pagamento;
import br.com.brunno.yfood.domain.entity.Pedido;
import br.com.brunno.yfood.domain.entity.Restaurante;
import br.com.brunno.yfood.domain.entity.Usuario;
import br.com.brunno.yfood.infrastructure.PagamentoRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.validation.BindException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class FechaPagamentoControllerTest {
    Restaurante restaurante = new Restaurante("teste", List.of(FormaPagamento.dinheiro));
    Usuario usuario = new Usuario("teste@email.com", List.of(FormaPagamento.dinheiro));
    Pedido pedido = new Pedido() {
        @Override public Long getId() { return 1L; }
        @Override public BigDecimal getValor() { return BigDecimal.TEN; }
    };
    Pagamento pagamento = new Pagamento(FormaPagamento.dinheiro, restaurante, usuario, pedido);

    PagamentoRepository pagamentoRepository = Mockito.mock(PagamentoRepository.class);
    FechaPagamentoController fechaPagamentoController = new FechaPagamentoController(pagamentoRepository);

    @Test
    @DisplayName("O pagamento deve ser concluido")
    void concluiPagamento() throws BindException {
        Mockito.doReturn(Optional.of(pagamento)).when(pagamentoRepository).findById(1L);

        fechaPagamentoController.concluiPagamento(1L);

        Assertions.assertThat(pagamento.isConcluido()).isTrue();
    }

    @Test
    @DisplayName("Quando o pagamento nao for encontrado, deve-se lançar exceção")
    void pagamentoNaoEncontrado() throws BindException {
        Mockito.doReturn(Optional.empty()).when(pagamentoRepository).findById(1L);

        Assertions.assertThatThrownBy(() -> fechaPagamentoController.concluiPagamento(1L));
    }

    @Test
    @DisplayName("Caso o pagamento já tenha sido concluido, então deve-se lançar exceção")
    void pagamentoJaConcluido() throws BindException {
        Mockito.doReturn(Optional.of(pagamento)).when(pagamentoRepository).findById(1L);
        fechaPagamentoController.concluiPagamento(1L);

        Assertions.assertThatThrownBy(() -> fechaPagamentoController.concluiPagamento(1L));
    }

}
