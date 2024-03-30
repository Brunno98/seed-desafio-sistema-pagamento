package br.com.brunno.yfood.infrastructure;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

@ExcludeFromJacocoGeneratedReport
@RestController
@RequestMapping("/api")
public class ServicosExternosController {

    private Map<Long, Pedido> pedidos = Map.of(
            1l, new Pedido(1l, BigDecimal.TEN),
            2l, new Pedido(2l, BigDecimal.valueOf(20)),
            3l, new Pedido(3l, BigDecimal.valueOf(30))
    );

    @GetMapping("/pedido/{id}")
    public ResponseEntity<Pedido> buscaPedido(@PathVariable Long id) {
        Optional<Pedido> optionalPedido = Optional.ofNullable(this.pedidos.get(id));
        return optionalPedido
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    record Pedido(Long id, BigDecimal valor) {};
}
