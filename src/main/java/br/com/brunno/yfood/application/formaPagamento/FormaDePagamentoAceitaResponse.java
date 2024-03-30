package br.com.brunno.yfood.application.formaPagamento;

import br.com.brunno.yfood.domain.entity.FormaPagamento;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/* Contagem carga
    - FormaPagamento
    - FormaDePagamentoAceitaResponse::new
 */

public class FormaDePagamentoAceitaResponse {

    private final String id;
    private final String descricao;

    public FormaDePagamentoAceitaResponse(FormaPagamento formaDePagamento) {
        this.id = formaDePagamento.name();
        this.descricao = formaDePagamento.descricao;
    }

    public static List<FormaDePagamentoAceitaResponse> from(Collection<FormaPagamento> formasDePagamentoAceitas) {
        return formasDePagamentoAceitas.stream()
                .map(FormaDePagamentoAceitaResponse::new)
                .collect(Collectors.toList());
    }

    public String getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }
}
