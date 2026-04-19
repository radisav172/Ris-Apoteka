package io.github.apoteka.apotekaspring.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "stavkanabavke")
public class Stavkanabavke {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "kolicina", nullable = false)
    private Integer kolicina;

    @NotNull
    @Column(name = "nabavna_cena", nullable = false, precision = 10, scale = 2)
    private BigDecimal nabavnaCena;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "nabavka_id", nullable = false)
    private Nabavka nabavka;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "lek_id", nullable = false)
    private Lek lek;


}