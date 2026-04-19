package io.github.apoteka.apotekaspring.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "lek")
public class Lek {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 150)
    @NotNull
    @Column(name = "naziv", nullable = false, length = 150)
    private String naziv;

    @Size(max = 50)
    @NotNull
    @Column(name = "sifra", nullable = false, length = 50)
    private String sifra;

    @Size(max = 100)
    @NotNull
    @Column(name = "proizvodjac", nullable = false, length = 100)
    private String proizvodjac;

    @Size(max = 100)
    @Column(name = "oblik", length = 100)
    private String oblik;

    @Size(max = 50)
    @Column(name = "jacina_doze", length = 50)
    private String jacinaDoze;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "kolicina_na_stanju", nullable = false)
    private Integer kolicinaNaStanju;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "minimalna_zaliha", nullable = false)
    private Integer minimalnaZaliha;

    @Column(name = "rok_trajanja")
    private LocalDate rokTrajanja;

    @NotNull
    @Column(name = "cena", nullable = false, precision = 10, scale = 2)
    private BigDecimal cena;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "na_recept", nullable = false)
    private Boolean naRecept;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "kategorija_id")
    private Kategorijaleka kategorija;


}