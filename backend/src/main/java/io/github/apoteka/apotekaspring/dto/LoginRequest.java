package io.github.apoteka.apotekaspring.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    private String korisnickoIme;
    private String lozinka;
}
