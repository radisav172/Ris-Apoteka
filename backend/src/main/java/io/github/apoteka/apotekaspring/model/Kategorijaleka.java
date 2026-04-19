package io.github.apoteka.apotekaspring.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "kategorijaleka")
public class Kategorijaleka {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @NotNull
    @Column(name = "naziv", nullable = false, length = 100)
    private String naziv;

    @Column(name = "opis", length = Integer.MAX_VALUE)
    private String opis;


}