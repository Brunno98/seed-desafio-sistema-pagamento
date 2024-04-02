package br.com.brunno.yfood.infrastructure;

import br.com.brunno.yfood.domain.entity.Pedido;
import br.com.brunno.yfood.domain.service.PedidoService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Optional;

@ExcludeFromJacocoGeneratedReport
@Component
public class PedidoClient implements PedidoService {

    @Override
    public Optional<Pedido> buscaPedidoPorId(Long id) {
        ResponseEntity<PedidoResponse> pedidoResponseEntity = null;
        try {
            pedidoResponseEntity = new RestTemplate()
                    .getForEntity("http://localhost:8080/api/pedido/" + id, PedidoResponse.class);
        } catch (RestClientException e) {
            if (e instanceof HttpClientErrorException.BadRequest) {
                return Optional.empty();
            }
            throw e;
        }

        if (pedidoResponseEntity.getStatusCode().is4xxClientError()) {
            return Optional.empty();
        }

        return Optional.ofNullable(pedidoResponseEntity.getBody());
    }

    record PedidoResponse(Long id, BigDecimal valor) implements Pedido {
        @Override
        public Long getId() {
            return this.id;
        }

        @Override
        public BigDecimal getValor() {
            return this.valor;
        }
    }
}
