package br.com.brunno.yfood.application.formaPagamento;

import br.com.brunno.yfood.domain.entity.FormaPagamento;
import br.com.brunno.yfood.domain.entity.Restaurante;
import br.com.brunno.yfood.domain.entity.Usuario;
import br.com.brunno.yfood.domain.service.ExecutaTransacao;
import br.com.brunno.yfood.domain.service.RegraFraude;
import jakarta.persistence.EntityManager;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/* contagem cognitiva:
    - FormaDePagamentoAceitaResponse
    - ConsultaFormaPagamentoRequest
    - Usuario
    - Restaurante
    - FormaPagamento
    - ExecutaTransacao
    - .executa(() -> entityManager.persist(usuario))
*/
@RestController
public class FormaDePagamentoController {

    @Autowired
    EntityManager entityManager;

    @Autowired
    ExecutaTransacao executaTransacao;

    @Autowired
    private List<RegraFraude> regrasFraude;

    @Value("${cache.usuario-seleciona-restaurante.tempo}")
    private Long ttl;

    @GetMapping("/formaPagamentoDisponivel")
    public ResponseEntity<List<FormaDePagamentoAceitaResponse>> formasDePagamentoDisponiveis(@Valid ConsultaFormaPagamentoRequest request) {
        Usuario usuario = entityManager.find(Usuario.class, request.getIdUsuario());
        Restaurante restaurante = entityManager.find(Restaurante.class, request.getIdRestaurante());

        List<FormaPagamento> formasDePagamentoAceitas =
                restaurante.formasDePagamentosAceitasParaUsuario(usuario, regrasFraude);

        if (usuario.selecionou(restaurante) < 2) {
            usuario.registraSelecao(restaurante);
            executaTransacao.executa(() -> entityManager.persist(usuario));
        } else {
            return ResponseEntity.ok()
                    .header("Expires", LocalDateTime.now().plusSeconds(ttl)
                            .format(DateTimeFormatter.ISO_DATE_TIME))
                    .body(FormaDePagamentoAceitaResponse.from(formasDePagamentoAceitas));
        }

        return ResponseEntity.ok(FormaDePagamentoAceitaResponse.from(formasDePagamentoAceitas));
    }

}
