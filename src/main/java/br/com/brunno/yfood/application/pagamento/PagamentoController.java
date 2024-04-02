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
 */

@RestController
public class PagamentoController {

    private final EntityManager entityManager;
    private final FormaPagamentoOfflinePedidoValidador formaPagamentoPedidoValidador;
    private final FormaDePagamentoParaPedidoValidator formaDePagamentoParaPedidoValidator;
    private final PedidoService pedidoService;

    @Autowired
    public PagamentoController(EntityManager entityManager,
                               FormaPagamentoOfflinePedidoValidador formaPagamentoPedidoValidador,
                               FormaDePagamentoParaPedidoValidator formaDePagamentoParaPedidoValidator,
                               PedidoService pedidoService) {
        this.entityManager = entityManager;
        this.formaPagamentoPedidoValidador = formaPagamentoPedidoValidador;
        this.formaDePagamentoParaPedidoValidator = formaDePagamentoParaPedidoValidator;
        this.pedidoService = pedidoService;
    }

    @InitBinder
    public void binder(WebDataBinder binder) {
        binder.addValidators(formaPagamentoPedidoValidador);
        binder.addValidators(formaDePagamentoParaPedidoValidator);
    }

    @Transactional
    @PostMapping("/pagamento/offline")
    public String geraPagamentoOfflineDePedido(@RequestBody @Valid NovoPedidoComPagamentoOfflineRequest request) throws BindException {
        Optional<Pedido> optionalPedido = pedidoService.buscaPedidoPorId(request.getIdPedido());
        if (optionalPedido.isEmpty()) {
            BindException bindException = new BindException(request, "novoPedidoComPagamentoOfflineRequest");
            bindException.rejectValue("idPedido", null, "pedido nao encontrado");
            throw bindException;
        }

        Pagamento pagamento = request.toPagamento(entityManager, optionalPedido.get());

        entityManager.merge(pagamento);

        return pagamento.toString();
    }

}
