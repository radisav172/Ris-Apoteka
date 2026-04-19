package io.github.apoteka.apotekaspring.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String ime;
    private String prezime;
    private String korisnickoIme;
    private String lozinka;
    private String rola;
}
