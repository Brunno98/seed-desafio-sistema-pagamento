package br.com.brunno.yfood.application.pagamento;

import br.com.brunno.yfood.domain.entity.FormaPagamento;
import br.com.brunno.yfood.domain.entity.Pedido;
import br.com.brunno.yfood.domain.entity.Restaurante;
import br.com.brunno.yfood.domain.entity.Usuario;
import br.com.brunno.yfood.domain.service.PedidoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager
class IniciaPagamentoOfflineControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TestEntityManager entityManager;

    @MockBean
    PedidoService pedidoService;

    Pedido pedido = new Pedido() {
        @Override public Long getId() { return 1L; }
        @Override public BigDecimal getValor() { return BigDecimal.valueOf(42); }
    };


    @DisplayName("Deve gerar um pedido")
    @Test
    @Transactional
    void test() throws Exception {
        Mockito.doReturn(Optional.of(pedido)).when(pedidoService).buscaPedidoPorId(1L);
        entityManager.persist(new Usuario("test@email", List.of(FormaPagamento.dinheiro)));
        entityManager.persist(new Restaurante("Restaurante de teste", List.of(FormaPagamento.dinheiro)));
        mockMvc.perform(post("/pagamento/offline")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(Map.of(
                        "idPedido", 1L,
                        "idUsuario", 1L,
                        "idRestaurante", 1L,
                        "formaPagamento", "dinheiro"
                ))))
                .andExpect(status().isOk());
    }

    @DisplayName("Deve retornar bad request caso o pedido nao seja encontrado")
    @Test
    @Transactional
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void pedidoNaoEncontrado() throws Exception {
        Mockito.doReturn(Optional.empty()).when(pedidoService).buscaPedidoPorId(1L);
        entityManager.persist(new Usuario("test@email", List.of(FormaPagamento.dinheiro)));
        entityManager.persist(new Restaurante("Restaurante de teste", List.of(FormaPagamento.dinheiro)));
        mockMvc.perform(post("/pagamento/offline")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(Map.of(
                                "idPedido", 1L,
                                "idUsuario", 1L,
                                "idRestaurante", 1L,
                                "formaPagamento", "dinheiro"
                        ))))
                .andExpect(status().isBadRequest());

        Mockito.verify(pedidoService).buscaPedidoPorId(1L);
    }

}