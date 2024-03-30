package br.com.brunno.yfood.domain.service;

import br.com.brunno.yfood.domain.entity.Pedido;

import java.util.Optional;

public interface PedidoService {

    Optional<Pedido> buscaPedidoPorId(Long id);

}
