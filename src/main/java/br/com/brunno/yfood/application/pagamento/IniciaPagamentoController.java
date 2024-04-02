package br.com.brunno.yfood.application.pagamento;

import br.com.brunno.yfood.domain.entity.Pagamento;
import br.com.brunno.yfood.domain.entity.Pedido;
import br.com.brunno.yfood.domain.service.PedidoService;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    public IniciaPagamentoController(EntityManager entityManager,
                                     FormaPagamentoOfflinePedidoValidador formaPagamentoPedidoValidador,
                                     FormaDePagamentoParaPedidoValidator formaDePagamentoParaPedidoValidator,
                                     FormaPagamentoOnlinePedidoValdador formaPagamentoOnlinePedidoValdador,
                                     PedidoService pedidoService) {
        this.entityManager = entityManager;
        this.formaPagamentoPedidoValidador = formaPagamentoPedidoValidador;
        this.formaDePagamentoParaPedidoValidator = formaDePagamentoParaPedidoValidator;
        this.formaPagamentoOnlinePedidoValdador = formaPagamentoOnlinePedidoValdador;
        this.pedidoService = pedidoService;
    }

    @InitBinder(value = "novoPedidoComPagamentoOfflineRequest")
    public void binderPagamentoOffline(WebDataBinder binder) {
        binder.addValidators(formaPagamentoPedidoValidador);
        binder.addValidators(formaDePagamentoParaPedidoValidator);
    }

    @InitBinder(value = "novoPedidoComPagamentoOnlineRequest")
    public void binderPagamentoOnline(WebDataBinder binder) {
        binder.addValidators(formaPagamentoOnlinePedidoValdador);
//        binder.addValidators(formaDePagamentoParaPedidoValidator);
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

        return "pagamento online...";
    }


}
