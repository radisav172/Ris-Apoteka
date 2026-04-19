package io.github.apoteka.apotekaspring.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "korisnik")
public class Korisnik {
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

    @Size(max = 50)
    @NotNull
    @Column(name = "korisnicko_ime", nullable = false, length = 50)
    private String korisnickoIme;

    @Size(max = 255)
    @NotNull
    @Column(name = "lozinka_hash", nullable = false)
    private String lozinkaHash;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @ColumnDefault("'korisnik'")
    @Column(name = "rola", columnDefinition = "rola_tip", nullable = false)
    private RolaTip rola = RolaTip.korisnik;
}
