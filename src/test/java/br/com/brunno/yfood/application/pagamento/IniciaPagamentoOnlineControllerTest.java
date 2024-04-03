package br.com.brunno.yfood.application.pagamento;

import br.com.brunno.yfood.domain.entity.FormaPagamento;
import br.com.brunno.yfood.domain.entity.GatewayPagamento;
import br.com.brunno.yfood.domain.entity.Pagamento;
import br.com.brunno.yfood.domain.entity.Pedido;
import br.com.brunno.yfood.domain.entity.StatusTransacao;
import br.com.brunno.yfood.domain.entity.Transacao;
import br.com.brunno.yfood.domain.service.PedidoService;
import br.com.brunno.yfood.domain.service.gatewayPagamento.Gateways;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class IniciaPagamentoOnlineControllerTest {

    @Autowired
    MockMvc mockMvc;

    Pedido pedido = new Pedido() {
        @Override public Long getId() { return 1L; }
        @Override public BigDecimal getValor() { return BigDecimal.valueOf(20);}
    };

    GatewayPagamento gatewayPagamentoDeTest = new GatewayPagamento() {
        @Override public Transacao processa(Pagamento pagamento) {
            return new Transacao(StatusTransacao.concluido);
        }

        @Override public boolean aceita(Pagamento pagamento) {
            return true;
        }

        @Override public BigDecimal custo(Pagamento pagamento) {
            return BigDecimal.ZERO;
        }
    };

    GatewayPagamento gatewayPagamentoComDefeito = new GatewayPagamento() {
        @Override public Transacao processa(Pagamento pagamento) {
            return new Transacao(StatusTransacao.falha);
        }

        @Override public boolean aceita(Pagamento pagamento) {
            return true;
        }

        @Override public BigDecimal custo(Pagamento pagamento) {
            return BigDecimal.ZERO;
        }
    };

    @MockBean
    PedidoService pedidoService;

    @SpyBean
    Gateways gateways;

    @Test
    @DisplayName("Deve iniciar pagamento online, realizar a tarifacao e concluir o pagamento")
    void test() throws Exception {
        ReflectionTestUtils.setField(gateways, "gateways", List.of(gatewayPagamentoDeTest));
        Mockito.doReturn(Optional.of(pedido)).when(pedidoService).buscaPedidoPorId(1L);

        mockMvc.perform(post("/restaurante")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(Map.of(
                        "nome", "teste restaurante",
                        "formasDePagamentoAceitas", Arrays.asList(FormaPagamento.values())
                )))).andExpect(status().isOk());

        mockMvc.perform(post("/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(Map.of(
                        "email", "teste@email.com",
                        "formasDePagamento", Arrays.asList(FormaPagamento.values())
                )))).andExpect(status().isOk());

        mockMvc.perform(post("/pagamento/online")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(Map.of(
                        "idPedido", 1L,
                        "idUsuario", 1L,
                        "idRestaurante", 1L,
                        "formaPagamento", FormaPagamento.elo,
                        "numeroCartao", "54515720315319335451572031531933",
                        "codigoSeguranca", "123"
                ))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar PAYMENT_REQUIRED quando nao for possivel tarifar")
    void test2() throws Exception {
        ReflectionTestUtils.setField(gateways, "gateways", List.of(gatewayPagamentoComDefeito));
        Mockito.doReturn(Optional.of(pedido)).when(pedidoService).buscaPedidoPorId(1L);

        mockMvc.perform(post("/restaurante")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(Map.of(
                        "nome", "teste restaurante",
                        "formasDePagamentoAceitas", Arrays.asList(FormaPagamento.values())
                )))).andExpect(status().isOk());

        mockMvc.perform(post("/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(Map.of(
                        "email", "teste@email.com",
                        "formasDePagamento", Arrays.asList(FormaPagamento.values())
                )))).andExpect(status().isOk());

        mockMvc.perform(post("/pagamento/online")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(Map.of(
                                "idPedido", 1L,
                                "idUsuario", 1L,
                                "idRestaurante", 1L,
                                "formaPagamento", FormaPagamento.elo,
                                "numeroCartao", "54515720315319335451572031531933",
                                "codigoSeguranca", "123"
                        ))))
                .andExpect(status().isPaymentRequired());
    }

    @Test
    @DisplayName("Deve retornar NOT_FOUND quando pedido nao for encontrado")
    void test3() throws Exception {
        ReflectionTestUtils.setField(gateways, "gateways", List.of(gatewayPagamentoDeTest));
        Mockito.doReturn(Optional.empty()).when(pedidoService).buscaPedidoPorId(1L);

        mockMvc.perform(post("/restaurante")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(Map.of(
                        "nome", "teste restaurante",
                        "formasDePagamentoAceitas", Arrays.asList(FormaPagamento.values())
                )))).andExpect(status().isOk());

        mockMvc.perform(post("/usuario")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(Map.of(
                        "email", "teste@email.com",
                        "formasDePagamento", Arrays.asList(FormaPagamento.values())
                )))).andExpect(status().isOk());

        mockMvc.perform(post("/pagamento/online")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(Map.of(
                                "idPedido", 1L,
                                "idUsuario", 1L,
                                "idRestaurante", 1L,
                                "formaPagamento", FormaPagamento.elo,
                                "numeroCartao", "54515720315319335451572031531933",
                                "codigoSeguranca", "123"
                        ))))
                .andExpect(status().isNotFound());
    }


}