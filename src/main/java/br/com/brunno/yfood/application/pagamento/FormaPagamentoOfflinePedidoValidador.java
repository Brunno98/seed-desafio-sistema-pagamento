package br.com.brunno.yfood.application.pagamento;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class FormaPagamentoOfflinePedidoValidador implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return NovoPedidoComPagamentoOfflineRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.hasErrors()) return;

        NovoPedidoComPagamentoOfflineRequest request = (NovoPedidoComPagamentoOfflineRequest) target;

        if (request.getFormaPagamento().online) {
            errors.rejectValue("formaPagamento", null, "precisa ser uma forma de pagamento offline");
        }
    }
}
