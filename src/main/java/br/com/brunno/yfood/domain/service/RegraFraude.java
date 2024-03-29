package br.com.brunno.yfood.domain.service;

import br.com.brunno.yfood.domain.entity.FormaPagamento;
import br.com.brunno.yfood.domain.entity.Usuario;

public interface RegraFraude {

    boolean verifica(Usuario usuario, FormaPagamento formaPagamento);

}
