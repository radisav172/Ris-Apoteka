package io.github.apoteka.apotekaspring.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "prodaja")
public class Prodaja {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "datum", nullable = false)
    private Instant datum;

    @Column(name = "napomena", length = Integer.MAX_VALUE)
    private String napomena;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "korisnik_id", nullable = false)
    private Korisnik korisnik;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "pacijent_id")
    private Pacijent pacijent;

    @OneToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "recept_id")
    private Recept recept;


}