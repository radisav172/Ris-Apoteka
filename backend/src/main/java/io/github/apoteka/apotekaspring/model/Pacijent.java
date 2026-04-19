package io.github.apoteka.apotekaspring.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "pacijent")
public class Pacijent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "ime", nullable = false, length = 100)
    private String ime;

    @Size(max = 100)
    @NotNull
    @Column(name = "prezime", nullable = false, length = 100)
    private String prezime;

    @Size(max = 13)
    @NotNull
    @Column(name = "jmbg", nullable = false, length = 13)
    private String jmbg;

    @Column(name = "datum_rodjenja")
    private LocalDate datumRodjenja;

    @Size(max = 100)
    @Column(name = "kontakt", length = 100)
    private String kontakt;


}