package br.com.brunno.yfood.application.pagamento;

import br.com.brunno.yfood.domain.entity.Pagamento;
import br.com.brunno.yfood.infrastructure.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

/*
    Contagem carga cognitiva:
    - Pagamento
    - PagamentoRepository
    - if (optionalPagamento.isEmpty())
    - if (pagamento.isConcluido())
 */

@RestController
public class FechaPagamentoController {

    private final PagamentoRepository pagamentoRepository;

    @Autowired
    public FechaPagamentoController(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    @PostMapping("/pagamento/{id}/conclui")
    public String concluiPagamento(@PathVariable Long id) throws BindException {
        Optional<Pagamento> optionalPagamento = pagamentoRepository.findById(id);
        if (optionalPagamento.isEmpty()) {
            BindException bindException = new BindException(id, "pagamentoId");
            bindException.reject(null, "pedido de pagamento nao encontrado");
            throw bindException;
        }
        Pagamento pagamento = optionalPagamento.get();

        if (pagamento.isConcluido()) {
            BindException bindException = new BindException(id, "pagamentoId");
            bindException.reject(null, "Pagamento "+id+" ja está concluido");
            throw bindException;
        }

        pagamento.conclui();

        pagamentoRepository.save(pagamento);

        return pagamento.toString();
    }

}
