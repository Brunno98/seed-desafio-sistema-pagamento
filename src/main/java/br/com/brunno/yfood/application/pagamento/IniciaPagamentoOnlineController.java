package br.com.brunno.yfood.application.pagamento;

import br.com.brunno.yfood.domain.entity.GatewayPagamento;
import br.com.brunno.yfood.domain.entity.Pagamento;
import br.com.brunno.yfood.domain.entity.Pedido;
import br.com.brunno.yfood.domain.entity.Transacao;
import br.com.brunno.yfood.domain.service.ExecutaTransacao;
import br.com.brunno.yfood.domain.service.PedidoService;
import br.com.brunno.yfood.domain.service.gatewayPagamento.Gateways;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/*
    contagem carga cognitiva:
    - PedidoService
    - ExecutaTransacao
    - FormaDePagamentoParaPedidoValidator
    - Gateways
    - NovoPedidoComPagamentoOnlineRequest
    - Pedido
    - if (possivelPedido.isEmpty())
    - Pagamento
    - executaTransacao.executa(() -> entityManager.persist(pagamento));
    - executaTransacao.executa(() -> entityManager.persist(pagamento));
    - if (!pagamento.isConcluido()) {

    total: 11
 */

@RestController
public class IniciaPagamentoOnlineController {

    private final EntityManager entityManager;
    private final PedidoService pedidoService;
    private final ExecutaTransacao executaTransacao;
    private final FormaDePagamentoParaPedidoValidator formaDePagamentoParaPedidoValidator;
    private final Gateways gatewaysPagamento;

    @Autowired
    public IniciaPagamentoOnlineController(EntityManager entityManager,
                                           PedidoService pedidoService,
                                           ExecutaTransacao executaTransacao,
                                           FormaDePagamentoParaPedidoValidator formaDePagamentoParaPedidoValidator,
                                           Gateways gatewaysPagamento) {
        this.entityManager = entityManager;
        this.pedidoService = pedidoService;
        this.executaTransacao = executaTransacao;
        this.formaDePagamentoParaPedidoValidator = formaDePagamentoParaPedidoValidator;
        this.gatewaysPagamento = gatewaysPagamento;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(formaDePagamentoParaPedidoValidator);
    }

    @PostMapping("/pagamento/online")
    public String geraPagamentoOnline(@RequestBody @Valid NovoPedidoComPagamentoOnlineRequest request) {
        Optional<Pedido> possivelPedido = pedidoService.buscaPedidoPorId(request.getIdPedido());
        if (possivelPedido.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        Pedido pedido = possivelPedido.get();

        Pagamento pagamento = request.toPagamento(entityManager, pedido);

        executaTransacao.executa(() -> entityManager.persist(pagamento));

        List<Transacao> transacoes = gatewaysPagamento.processa(pagamento);

        pagamento.registraTransacoes(transacoes);

        executaTransacao.executa(() -> entityManager.persist(pagamento));

        if (!pagamento.isConcluido()) {
            throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED);
        }

        return "pagamento online...";
    }
}
