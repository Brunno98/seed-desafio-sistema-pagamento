package br.com.brunno.yfood.domain;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    @ElementCollection
    private Set<FormaPagamento> formasDePagametoPossiveis = new HashSet<>();

    @Deprecated
    public Usuario() {}

    public Usuario(String email, List<FormaPagamento> formasDePagametoPossiveis) {
        Assert.hasText(email, "Usuario precisa ter email");
        Assert.isTrue(!formasDePagametoPossiveis.isEmpty(), "Usuario precisa ter, pelo menos, uma forma de pagamento possivel");
        this.email = email;
        this.formasDePagametoPossiveis.addAll(formasDePagametoPossiveis);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", formasDePagametoPossiveis=" + formasDePagametoPossiveis +
                '}';
    }

    public boolean possuiFormaDePagamento(FormaPagamento formaDePagamento) {
        return this.formasDePagametoPossiveis.stream().anyMatch(formaDePagamento::equals);
    }
}
