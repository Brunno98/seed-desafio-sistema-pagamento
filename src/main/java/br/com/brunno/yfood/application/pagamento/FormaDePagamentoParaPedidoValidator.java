package br.com.brunno.yfood.application.pagamento;

import br.com.brunno.yfood.domain.entity.FormaPagamento;
import br.com.brunno.yfood.domain.entity.Restaurante;
import br.com.brunno.yfood.domain.entity.Usuario;
import br.com.brunno.yfood.domain.service.RegraFraude;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class FormaDePagamentoParaPedidoValidator implements Validator {

    private final EntityManager entityManager;
    private final List<RegraFraude> regrasFraude;

    @Autowired
    public FormaDePagamentoParaPedidoValidator(EntityManager entityManager, List<RegraFraude> regrasFraude) {
        this.entityManager = entityManager;
        this.regrasFraude = regrasFraude;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return PedidoPagamentoRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.hasErrors()) return;

        PedidoPagamentoRequest request = (PedidoPagamentoRequest) target;

        Restaurante restaurante = entityManager.find(Restaurante.class, request.getIdRestaurante());
        Usuario usuario = entityManager.find(Usuario.class, request.getIdUsuario());

        List<FormaPagamento> formasPagamentoAceitas = restaurante.formasDePagamentosAceitasParaUsuario(usuario, regrasFraude);

        FormaPagamento formaPagamento = request.getFormaPagamento();
        if (!formasPagamentoAceitas.contains(formaPagamento)) {
            errors.reject(null, "Forma de pagamento "+formaPagamento+" nao disponivel para usuario ou restaurante");
        }
    }
}
