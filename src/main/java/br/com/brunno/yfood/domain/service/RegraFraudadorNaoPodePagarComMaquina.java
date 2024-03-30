package br.com.brunno.yfood.domain.service;

import br.com.brunno.yfood.domain.entity.FormaPagamento;
import br.com.brunno.yfood.domain.entity.Usuario;
import org.springframework.stereotype.Service;

@Service
public class RegraFraudadorNaoPodePagarComMaquina implements RegraFraude{
    @Override
    public boolean verifica(Usuario usuario, FormaPagamento formaPagamento) {
        if (!FormaPagamento.maquina.equals(formaPagamento)) return false;

        return "fraudador@email.com".equals(usuario.getEmail());
    }
}
