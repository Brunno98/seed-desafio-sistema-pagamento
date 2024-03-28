package br.com.brunno.yfood.application;

import br.com.brunno.yfood.domain.FormaPagamento;
import br.com.brunno.yfood.domain.Restaurante;
import br.com.brunno.yfood.domain.Usuario;
import br.com.brunno.yfood.infrastructure.validators.Exists;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/* contagem cognitiva:
    - FormaDePagamentoAceitaResponse
    - ConsultaFormaPagamentoRequest
    - Usuario
    - Restaurante
    - FormaPagamento
*/
@RestController
public class FormaDePagamentoController {

    @Autowired
    EntityManager entityManager;

    @GetMapping("/formaPagamentoDisponivel")
    public List<FormaDePagamentoAceitaResponse> formasDePagamentoDisponiveis(@Valid ConsultaFormaPagamentoRequest request) {
        Usuario usuario = entityManager.find(Usuario.class, request.getIdUsuario());
        Restaurante restaurante = entityManager.find(Restaurante.class, request.getIdRestaurante());

        List<FormaPagamento> formasDePagamentoAceitas =
                restaurante.formasDePagamentosAceitasParaOUsuario(usuario);

        return FormaDePagamentoAceitaResponse.from(formasDePagamentoAceitas);
    }

}
