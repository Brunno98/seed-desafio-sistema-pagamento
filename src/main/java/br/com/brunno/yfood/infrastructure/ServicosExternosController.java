package br.com.brunno.yfood.infrastructure;

import com.fasterxml.jackson.annotation.JsonAlias;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static java.lang.Thread.sleep;

@ExcludeFromJacocoGeneratedReport
@RestController
public class ServicosExternosController {
    public static final Logger log = LoggerFactory.getLogger(ServicosExternosController.class);

    private Map<Long, Pedido> pedidos = Map.of(
            1l, new Pedido(1l, BigDecimal.TEN),
            2l, new Pedido(2l, BigDecimal.valueOf(20)),
            3l, new Pedido(3l, BigDecimal.valueOf(30)),
            4l, new Pedido(4l, BigDecimal.valueOf(99999))
    );

    @GetMapping("/api/pedido/{id}")
    public ResponseEntity<Pedido> buscaPedido(@PathVariable Long id) {
        Optional<Pedido> optionalPedido = Optional.ofNullable(this.pedidos.get(id));
        return optionalPedido
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    record Pedido(Long id, BigDecimal valor) {};

    @PostMapping("/validar")
    public ResponseEntity<?> validaCartao(@RequestBody ValidaCartao validaCartao) throws InterruptedException {
        log.info(String.format("validando cartao [numero: %s - cvv: %s]",
                        validaCartao.numCartao, validaCartao.codigoSeguranca));
        sleep(250);
        log.info("Cartao validado! gerando id de processamento...");

        String idProcessamento = UUID.randomUUID().toString();

        log.info("gerado id de processamento: "+idProcessamento);
        return ResponseEntity.ok(Map.of("idProcessamento", idProcessamento));
    }

    @PostMapping("/tarifa/{idProcessamento}")
    public void processaPagamento(@PathVariable String idProcessamento) throws InterruptedException {
        log.info("processando id de pagamento: " + idProcessamento);
        sleep(250);
        log.info("Pagamento "+idProcessamento+" processado com sucesso");
    }

    @PostMapping("/tarifa")
    public void processaPagamento() throws InterruptedException {
        log.info("processando pagamento...");
        sleep(250);
        log.info("Pagamento processado com sucesso");
    }

    public record ValidaCartao(@JsonAlias("codigo_seguraca") String codigoSeguranca, @JsonAlias("num_cartao") String numCartao) {}
}
