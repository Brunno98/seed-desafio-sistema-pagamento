package br.com.brunno.yfood.application.usuario;

import br.com.brunno.yfood.domain.entity.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/* carga cognitiva:
    - NovoUsuarioRequest
    - Usuario
 */

@RestController
public class UsuarioController {

    @Autowired
    private EntityManager entityManager;

    @Transactional
    @PostMapping("/usuario")
    public String criaUsuario(@RequestBody @Valid NovoUsuarioRequest request) {
        Usuario novoUsuario = request.toUsuario();

        entityManager.persist(novoUsuario);

        return novoUsuario.toString();
    }
}
