package io.github.apoteka.apotekaspring.service;

import io.github.apoteka.apotekaspring.model.Lek;
import io.github.apoteka.apotekaspring.repository.LekRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LekService {

    private final LekRepository repository;

    public List<Lek> findAll() {
        return repository.findAll();
    }

    public Lek findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lek nije pronađen"));
    }

    public List<Lek> findNiskeZalihe() {
        return repository.findNiskeZalihe();
    }

    public List<Lek> findIsticuRok(int danaDo) {
        LocalDate danas = LocalDate.now();
        return repository.findIsticuRok(danas, danas.plusDays(danaDo));
    }

    public Lek save(Lek lek) {
        return repository.save(lek);
    }

    public void delete(Integer id) {
        findById(id);
        repository.deleteById(id);
    }

    public Lek update(Integer id, Lek lek) {
        Lek existing = findById(id);
        existing.setNaziv(lek.getNaziv());
        existing.setSifra(lek.getSifra());
        existing.setProizvodjac(lek.getProizvodjac());
        existing.setOblik(lek.getOblik());
        existing.setJacinaDoze(lek.getJacinaDoze());
        existing.setKolicinaNaStanju(lek.getKolicinaNaStanju());
        existing.setMinimalnaZaliha(lek.getMinimalnaZaliha());
        existing.setRokTrajanja(lek.getRokTrajanja());
        existing.setCena(lek.getCena());
        existing.setNaRecept(lek.getNaRecept());
        existing.setKategorija(lek.getKategorija());
        return repository.save(existing);
    }
}
