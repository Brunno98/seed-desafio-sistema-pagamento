package br.com.brunno.yfood.infrastructure;

import br.com.brunno.yfood.domain.FormaPagamento;
import br.com.brunno.yfood.domain.RegraFraude;
import br.com.brunno.yfood.domain.Usuario;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegraEmailFraudador implements RegraFraude {

    private final List<String> emailsFraudadores = List.of("fraudador@email.com");

    @Override
    public boolean verifica(Usuario usuario, FormaPagamento formaPagamento) {
        if (!formaPagamento.online) return false;

        return emailsFraudadores.stream()
                .anyMatch(fraudador -> fraudador.equals(usuario.getEmail()));
    }
}
