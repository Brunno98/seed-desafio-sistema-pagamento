package br.com.brunno.yfood.domain.entity;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.math.BigDecimal;
import java.util.HashMap;

public interface Pedido {

    Long getId();

    BigDecimal getValor();

}
