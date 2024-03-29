package br.com.brunno.yfood.application;

import br.com.brunno.yfood.domain.entity.FormaPagamento;
import br.com.brunno.yfood.domain.entity.Restaurante;
import br.com.brunno.yfood.domain.entity.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FormaPagamentoControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    EntityManager entityManager;

    /*
    a[] e b[]
    1 elemento de a está em b
    0 elemento de a está em b
     */

    @ParameterizedTest
    @MethodSource("geraListaDeFormasDePagamento")
    @DisplayName("Deve retornar as formas de pagamento disponiveis entre restaurante e usuario")
    @Transactional
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void test(
            List<FormaPagamento> formasPagamentoUsuario,
            List<FormaPagamento> formasPagamentoRestaurante,
            int totalFormasDisponiveisEsperadas
    ) {
        Usuario usuario = new Usuario("test@email.com", formasPagamentoUsuario);
        entityManager.persist(usuario);
        Restaurante restaurante = new Restaurante("Restaurante de teste", formasPagamentoRestaurante);
        entityManager.persist(restaurante);

        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/formaPagamentoDisponivel")
                    .queryParam("idUsuario", "1")
                    .queryParam("idRestaurante", "1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$", hasSize(totalFormasDisponiveisEsperadas)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Stream<Arguments> geraListaDeFormasDePagamento() {
        return Stream.of(
                Arguments.of(
                        List.of(FormaPagamento.dinheiro, FormaPagamento.mastercard),
                        List.of(FormaPagamento.elo, FormaPagamento.dinheiro),
                        1),
                Arguments.of(
                        List.of(FormaPagamento.cheque, FormaPagamento.mastercard),
                        List.of(FormaPagamento.elo, FormaPagamento.dinheiro),
                        0)
        );
    }

}
