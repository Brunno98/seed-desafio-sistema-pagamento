package br.com.brunno.yfood.infrastructure;

import br.com.brunno.yfood.domain.entity.Transacao;
import org.springframework.data.repository.CrudRepository;

public interface TransacaoRepository extends CrudRepository<Transacao, Long> {
}
