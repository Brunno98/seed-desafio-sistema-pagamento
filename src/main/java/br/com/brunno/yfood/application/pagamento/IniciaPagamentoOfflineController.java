package br.com.brunno.yfood.application.pagamento;

import br.com.brunno.yfood.domain.entity.GatewayPagamento;
import br.com.brunno.yfood.domain.entity.Pagamento;
import br.com.brunno.yfood.domain.entity.Pedido;
import br.com.brunno.yfood.domain.service.PedidoService;
import br.com.brunno.yfood.domain.service.gatewayPagamento.Seya;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
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
    Contagem carga cognitiva:
    - FormaPagamentoOfflinePedidoValidador
    - FormaDePagamentoParaPedidoValidator
    - NovoPedidoComPagamentoOfflineRequest
    - PedidoService
    - Pedido
    - Pagamento
    - if (optionalPedido.isEmpty())
 */

@RestController
public class IniciaPagamentoController {

    private final EntityManager entityManager;
    private final FormaPagamentoOfflinePedidoValidador formaPagamentoPedidoValidador;
    private final FormaDePagamentoParaPedidoValidator formaDePagamentoParaPedidoValidator;
    private final FormaPagamentoOnlinePedidoValdador formaPagamentoOnlinePedidoValdador;
    private final PedidoService pedidoService;
    private final List<GatewayPagamento> gatewaysPagamento;

    @Autowired
    public IniciaPagamentoController(EntityManager entityManager,
                                     FormaPagamentoOfflinePedidoValidador formaPagamentoPedidoValidador,
                                     FormaDePagamentoParaPedidoValidator formaDePagamentoParaPedidoValidator,
                                     FormaPagamentoOnlinePedidoValdador formaPagamentoOnlinePedidoValdador,
                                     PedidoService pedidoService,
                                     List<GatewayPagamento> gatewaysPagamento) {
        this.entityManager = entityManager;
        this.formaPagamentoPedidoValidador = formaPagamentoPedidoValidador;
        this.formaDePagamentoParaPedidoValidator = formaDePagamentoParaPedidoValidator;
        this.formaPagamentoOnlinePedidoValdador = formaPagamentoOnlinePedidoValdador;
        this.pedidoService = pedidoService;
        this.gatewaysPagamento = gatewaysPagamento;
    }

    @InitBinder(value = "novoPedidoComPagamentoOfflineRequest")
    public void binderPagamentoOffline(WebDataBinder binder) {
        binder.addValidators(formaPagamentoPedidoValidador);
        binder.addValidators(formaDePagamentoParaPedidoValidator);
    }

    @InitBinder(value = "novoPedidoComPagamentoOnlineRequest")
    public void binderPagamentoOnline(WebDataBinder binder) {
        binder.addValidators(formaPagamentoOnlinePedidoValdador);
        binder.addValidators(formaDePagamentoParaPedidoValidator);
    }

    @Transactional
    @PostMapping("/pagamento/offline")
    public String geraPagamentoOfflineDePedido(@RequestBody @Valid NovoPedidoComPagamentoOfflineRequest request) throws BindException {
        Optional<Pedido> optionalPedido = pedidoService.buscaPedidoPorId(request.getIdPedido());
        if (optionalPedido.isEmpty()) {
            BindException bindException = new BindException(request, "novoPedidoComPagamentoOfflineRequest");
            bindException.reject(null, "pedido "+request.getIdPedido()+" nao encontrado");
            throw bindException;
        }

        Pagamento pagamento = request.toPagamento(entityManager, optionalPedido.get());

        entityManager.persist(pagamento);

        return pagamento.toString();
    }

    @PostMapping("/pagamento/online")
    public String geraPagamentoOnline(@RequestBody @Valid NovoPedidoComPagamentoOnlineRequest request) {
        Optional<Pedido> possivelPedido = pedidoService.buscaPedidoPorId(request.getIdPedido());
        if (possivelPedido.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        Pedido pedido = possivelPedido.get();

        Pagamento pagamento = request.toPagamento(entityManager, pedido);

        GatewayPagamento gatewayPagamento = gatewaysPagamento.stream()
                .min(Comparator.comparing((gateway -> gateway.calculaTaxa(pagamento))))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.I_AM_A_TEAPOT));

        boolean resultadoPagamento = gatewayPagamento.executaPagamento(request.getNumeroCartao(), request.getCodigoSeguranca(), pedido.getValor());
        if (!resultadoPagamento) {
            throw new ResponseStatusException(HttpStatus.PAYMENT_REQUIRED);
        }

        return "pagamento online...";
    }


}
