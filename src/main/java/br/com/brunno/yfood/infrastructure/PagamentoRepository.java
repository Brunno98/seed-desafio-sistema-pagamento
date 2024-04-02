package br.com.brunno.yfood.infrastructure;

import br.com.brunno.yfood.domain.entity.Pagamento;
import org.springframework.data.repository.CrudRepository;

public interface PagamentoRepository extends CrudRepository<Pagamento, Long> {
}
