package br.com.brunno.yfood.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Restaurante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private Set<FormaPagamento> formasDePagamentoAceitas = new HashSet<>();

    @Deprecated
    public Restaurante() {}

    public Restaurante(String nome, List<FormaPagamento> formasDePagamentoAceitas) {
        Assert.hasText(nome, "Restaurante precisa ter nome");
        Assert.isTrue(!formasDePagamentoAceitas.isEmpty(), "Restaurante precisa aceitar, pelo menos, uma forma de pagamento");
        this.nome = nome;
        this.formasDePagamentoAceitas.addAll(formasDePagamentoAceitas);
    }

    @Override
    public String toString() {
        return "Restaurante{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", formasDePagamentoAceitas=" + formasDePagamentoAceitas +
                '}';
    }

    public List<FormaPagamento> formasDePagamentosAceitasParaUsuario(Usuario usuario, List<RegraFraude> regrasFraude) {
        return this.formasDePagamentoAceitas.stream()
                .filter(usuario::possuiFormaDePagamento)
                .filter(formaPagamento -> regrasFraude.stream()
                        .noneMatch(regraFraude -> regraFraude.verifica(usuario, formaPagamento)))
                .collect(Collectors.toList());
    }
}
