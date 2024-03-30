package br.com.brunno.yfood.application.pagamento;

import br.com.brunno.yfood.domain.entity.Pedido;
import br.com.brunno.yfood.domain.entity.Transacao;
import br.com.brunno.yfood.domain.service.ExecutaTransacao;
import br.com.brunno.yfood.domain.service.PedidoService;
import br.com.brunno.yfood.infrastructure.TransacaoRepository;
import jakarta.persistence.EntityManager;
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
    - Transacao
 */

@RestController
public class PagamentoController {

    private final EntityManager entityManager;
    private final FormaPagamentoOfflinePedidoValidador formaPagamentoPedidoValidador;
    private final FormaDePagamentoParaPedidoValidator formaDePagamentoParaPedidoValidator;
    private final PedidoService pedidoService;
    private final TransacaoRepository transacaoRepository;

    @Autowired
    public PagamentoController(EntityManager entityManager,
                               FormaPagamentoOfflinePedidoValidador formaPagamentoPedidoValidador,
                               FormaDePagamentoParaPedidoValidator formaDePagamentoParaPedidoValidator, PedidoService pedidoService,
                               TransacaoRepository transacaoRepository) {
        this.entityManager = entityManager;
        this.formaPagamentoPedidoValidador = formaPagamentoPedidoValidador;
        this.formaDePagamentoParaPedidoValidator = formaDePagamentoParaPedidoValidator;
        this.pedidoService = pedidoService;
        this.transacaoRepository = transacaoRepository;
    }

    @InitBinder
    public void binder(WebDataBinder binder) {
        binder.addValidators(formaPagamentoPedidoValidador);
        binder.addValidators(formaDePagamentoParaPedidoValidator);
    }

    @PostMapping("/pagamento/offline")
    public String geraPagamentoOfflineDePedido(@RequestBody @Valid NovoPedidoComPagamentoOfflineRequest request) throws BindException {
        Optional<Pedido> optionalPedido = pedidoService.buscaPedidoPorId(request.getIdPedido());
        if (optionalPedido.isEmpty()) {
            BindException bindException = new BindException(request, "novoPedidoComPagamentoOfflineRequest");
            bindException.rejectValue("idPedido", null, "pedido nao encontrado");
            throw bindException;
        }

        Transacao transacao = request.toTransacao(entityManager, optionalPedido.get());

        transacaoRepository.save(transacao);

        return transacao.toString();
    }
}