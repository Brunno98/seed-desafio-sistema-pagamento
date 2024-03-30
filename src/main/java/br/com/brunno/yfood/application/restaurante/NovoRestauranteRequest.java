package br.com.brunno.yfood.application.restaurante;

import br.com.brunno.yfood.domain.entity.FormaPagamento;
import br.com.brunno.yfood.domain.entity.Restaurante;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/* carga cognitiva:
    - FomarPagamento
    - Restaurante
 */

public class NovoRestauranteRequest {

    @NotBlank
    private String nome;

    @NotEmpty
    private List<FormaPagamento> formasDePagamentoAceitas;

    public String getNome() {
        return nome;
    }

    public List<FormaPagamento> getFormasDePagamentoAceitas() {
        return formasDePagamentoAceitas;
    }

    public Restaurante toRestaurante() {
        return new Restaurante(nome, formasDePagamentoAceitas);
    }
}
