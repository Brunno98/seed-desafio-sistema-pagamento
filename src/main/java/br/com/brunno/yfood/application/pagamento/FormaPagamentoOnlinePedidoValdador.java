package br.com.brunno.yfood.application.pagamento;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class FormaPagamentoOnlinePedidoValdador implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return NovoPedidoComPagamentoOnlineRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.hasErrors()) return;

        NovoPedidoComPagamentoOnlineRequest request = (NovoPedidoComPagamentoOnlineRequest) target;

        if (!request.getFormaPagamento().online) {
            errors.reject(null, "forma de pagamento precisa ser online");
        }

    }
}
