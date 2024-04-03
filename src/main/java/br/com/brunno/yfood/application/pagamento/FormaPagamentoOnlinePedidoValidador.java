package br.com.brunno.yfood.application.pagamento;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class FormaPagamentoOnlinePedidoValidador implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return NovoPedidoComPagamentoOnlineRequest.class.isAssignableFrom(clazz);
    }

    /*
    (a) tem erro
    (b) forma de pagamento online
    (=) invalido

    a  b  =
  1 v  v  v
  2 v  f  v
  3 f  v  f
  4 f  f  v

  (1,3), (3, 4) -> 1,3,4
     */

    @Override
    public void validate(Object target, Errors errors) {
        if (errors.hasErrors()) return;

        NovoPedidoComPagamentoOnlineRequest request = (NovoPedidoComPagamentoOnlineRequest) target;

        if (!request.getFormaPagamento().online) {
            errors.reject(null, "forma de pagamento precisa ser online");
        }

    }
}
