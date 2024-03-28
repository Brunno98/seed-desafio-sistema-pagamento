package br.com.brunno.yfood.application;

import br.com.brunno.yfood.domain.FormaPagamento;
import br.com.brunno.yfood.domain.Usuario;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/* carga cognitiva:
    - FormaPagamento
    - Usuario
 */

public class NovoUsuarioRequest {

    @NotBlank
    @Email
    private String email;

    @NotEmpty
    private List<FormaPagamento> formasDePagamento;

    public String getEmail() {
        return email;
    }

    public List<FormaPagamento> getFormasDePagamento() {
        return formasDePagamento;
    }

    public Usuario toUsuario() {
        return new Usuario(email, formasDePagamento);
    }
}
