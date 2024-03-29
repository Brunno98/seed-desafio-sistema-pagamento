package br.com.brunno.yfood.domain;

public interface RegraFraude {

    boolean verifica(Usuario usuario, FormaPagamento formaPagamento);

}
