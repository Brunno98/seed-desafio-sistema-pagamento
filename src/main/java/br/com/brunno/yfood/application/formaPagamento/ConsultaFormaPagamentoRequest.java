package br.com.brunno.yfood.application.formaPagamento;

import br.com.brunno.yfood.domain.entity.Restaurante;
import br.com.brunno.yfood.domain.entity.Usuario;
import br.com.brunno.yfood.infrastructure.validators.Exists;
import jakarta.validation.constraints.NotNull;

/* Carga cognitiva:
    - @Exists
    - Usuario
    - Restaurante
 */

public class ConsultaFormaPagamentoRequest {

    @NotNull
    @Exists(domain = Usuario.class, domainField = "id")
    private Long idUsuario;

    @NotNull
    @Exists(domain = Restaurante.class, domainField = "id")
    private Long idRestaurante;

    public ConsultaFormaPagamentoRequest(Long idUsuario, Long idRestaurante) {
        this.idUsuario = idUsuario;
        this.idRestaurante = idRestaurante;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public Long getIdRestaurante() {
        return idRestaurante;
    }
}
