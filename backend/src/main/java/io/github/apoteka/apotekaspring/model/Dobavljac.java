package io.github.apoteka.apotekaspring.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "dobavljac")
public class Dobavljac {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 150)
    @NotNull
    @Column(name = "naziv", nullable = false, length = 150)
    private String naziv;

    @Size(max = 255)
    @Column(name = "adresa")
    private String adresa;

    @Size(max = 100)
    @Column(name = "kontakt", length = 100)
    private String kontakt;

    @Size(max = 20)
    @NotNull
    @Column(name = "pib", nullable = false, length = 20)
    private String pib;


}