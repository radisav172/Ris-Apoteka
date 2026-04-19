package io.github.apoteka.apotekaspring.repository;

import io.github.apoteka.apotekaspring.model.Korisnik;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KorisnikRepository extends JpaRepository<Korisnik, Integer> {
    boolean existsByKorisnickoIme(String korisnickoIme);
    java.util.Optional<Korisnik> findByKorisnickoIme(String korisnickoIme);
}
