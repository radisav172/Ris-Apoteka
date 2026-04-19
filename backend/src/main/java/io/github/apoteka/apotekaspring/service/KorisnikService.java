package io.github.apoteka.apotekaspring.service;

import io.github.apoteka.apotekaspring.model.Korisnik;
import io.github.apoteka.apotekaspring.repository.KorisnikRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KorisnikService {

    private final KorisnikRepository korisnikRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Korisnik> getAll() {
        return korisnikRepository.findAll();
    }

    public Korisnik create(Korisnik korisnik) {
        if (korisnikRepository.existsByKorisnickoIme(korisnik.getKorisnickoIme())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Korisničko ime već postoji");
        }
        korisnik.setLozinkaHash(passwordEncoder.encode(korisnik.getLozinkaHash()));
        return korisnikRepository.save(korisnik);
    }

    public Korisnik update(Integer id, Korisnik korisnik) {
        Korisnik existing = korisnikRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Korisnik nije pronađen"));
        existing.setIme(korisnik.getIme());
        existing.setPrezime(korisnik.getPrezime());
        existing.setKorisnickoIme(korisnik.getKorisnickoIme());
        existing.setLozinkaHash(passwordEncoder.encode(korisnik.getLozinkaHash()));
        existing.setRola(korisnik.getRola());
        return korisnikRepository.save(existing);
    }

    public void delete(Integer id) {
        if (!korisnikRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Korisnik nije pronađen");
        }
        korisnikRepository.deleteById(id);
    }
}
