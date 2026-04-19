package io.github.apoteka.apotekaspring.service;

import io.github.apoteka.apotekaspring.model.Dobavljac;
import io.github.apoteka.apotekaspring.repository.DobavljacRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DobavljacService {

    private final DobavljacRepository repository;

    public List<Dobavljac> findAll() {
        return repository.findAll();
    }

    public Dobavljac findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dobavljač nije pronađen"));
    }

    public Dobavljac save(Dobavljac dobavljac) {
        return repository.save(dobavljac);
    }

    public void delete(Integer id) {
        findById(id);
        try {
            repository.deleteById(id);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Dobavljač se ne može obrisati jer postoje povezane nabavke");
        }
    }

    public Dobavljac update(Integer id, Dobavljac dobavljac) {
        Dobavljac existing = findById(id);
        existing.setNaziv(dobavljac.getNaziv());
        existing.setAdresa(dobavljac.getAdresa());
        existing.setKontakt(dobavljac.getKontakt());
        existing.setPib(dobavljac.getPib());
        return repository.save(existing);
    }
}
