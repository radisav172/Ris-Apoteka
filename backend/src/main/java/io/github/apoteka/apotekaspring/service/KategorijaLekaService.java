package io.github.apoteka.apotekaspring.service;

import io.github.apoteka.apotekaspring.model.Kategorijaleka;
import io.github.apoteka.apotekaspring.repository.KategorijaLekaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KategorijaLekaService {

    private final KategorijaLekaRepository repository;

    public List<Kategorijaleka> findAll() {
        return repository.findAll();
    }

    public Kategorijaleka findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Kategorija nije pronađena"));
    }

    public Kategorijaleka save(Kategorijaleka kategorija) {
        return repository.save(kategorija);
    }

    public Kategorijaleka update(Integer id, Kategorijaleka kategorija) {
        Kategorijaleka existing = findById(id);
        existing.setNaziv(kategorija.getNaziv());
        existing.setOpis(kategorija.getOpis());
        return repository.save(existing);
    }

    public void delete(Integer id) {
        findById(id);
        repository.deleteById(id);
    }
}
