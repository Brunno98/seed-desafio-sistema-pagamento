package br.com.brunno.yfood.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private StatusTransacao status = StatusTransacao.esperandoPagamento;

    private LocalDateTime instanteDeCriacao = LocalDateTime.now();

    @Deprecated
    public Transacao() {}

    public Transacao(StatusTransacao status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Transacao{" +
                "id=" + id +
                ", status=" + status +
                ", instanteDeCriacao=" + instanteDeCriacao +
                '}';
    }
}
