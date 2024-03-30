package br.com.brunno.yfood.domain.service;

import br.com.brunno.yfood.domain.entity.FormaPagamento;
import br.com.brunno.yfood.domain.entity.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegraEmailFraudador implements RegraFraude {

    private final List<String> emailsFraudadores = List.of("fraudador@email.com");

    /*
    (a) forma de pagamento online
    (b) email na lista de fraudadores

    a  b  =
  1 v  v  v
  2 v  f  f
  3 f  v  f
  4 f  f  f

   (1, 3), (1,2)
     */

    @Override
    public boolean verifica(Usuario usuario, FormaPagamento formaPagamento) {
        if (!formaPagamento.online) return false;

        return emailsFraudadores.stream()
                .anyMatch(fraudador -> fraudador.equals(usuario.getEmail()));
    }
}
