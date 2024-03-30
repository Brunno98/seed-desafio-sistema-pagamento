package br.com.brunno.yfood.application.restaurante;

import br.com.brunno.yfood.domain.entity.Restaurante;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/* carga cognitiva
    - NovoRestauranteRequest
    - Restaurante
 */

@RestController
public class RestauranteController {

    @Autowired
    private EntityManager entityManager;

    @Transactional
    @PostMapping("/restaurante")
    public String criaRestaurante(@RequestBody @Valid NovoRestauranteRequest request) {
        Restaurante novoRestaurante = request.toRestaurante();

        entityManager.persist(novoRestaurante);

        return novoRestaurante.toString();
    }

    @GetMapping("/restaurante/{id}")
    public String buscaRestaurantePorId(@PathVariable Long id) {
        Restaurante restaurante = entityManager.find(Restaurante.class, id);
        return restaurante.toString();
    }
}
