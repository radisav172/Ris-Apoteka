package io.github.apoteka.apotekaspring.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class RacunStavkaDto {
    private String naziv;
    private Integer kolicina;
    private BigDecimal cenaProdaje;
    private BigDecimal ukupno;
}
