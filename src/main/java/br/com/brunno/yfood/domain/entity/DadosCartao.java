package br.com.brunno.yfood.domain.entity;

public class DadosCartao {

    private String numeroCartao;

    private String codigoSeguranca;

    @Deprecated
    public DadosCartao() {}

    public DadosCartao(String numeroCartao, String codigoSeguranca) {
        this.numeroCartao = numeroCartao;
        this.codigoSeguranca = codigoSeguranca;
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public String getCodigoSeguranca() {
        return codigoSeguranca;
    }
}
