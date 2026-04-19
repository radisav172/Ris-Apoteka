package io.github.apoteka.apotekaspring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ZaliheStavkaDto {
    private String naziv;
    private String sifra;
    private String kategorija;
    private Integer kolicinaNaStanju;
    private Integer minimalnaZaliha;
    private BigDecimal cena;
    private String rokTrajanja;
    private Boolean nistaZaliha;
}
