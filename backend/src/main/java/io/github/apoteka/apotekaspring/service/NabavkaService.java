package io.github.apoteka.apotekaspring.service;

import io.github.apoteka.apotekaspring.model.Nabavka;
import io.github.apoteka.apotekaspring.repository.NabavkaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NabavkaService {

    private final NabavkaRepository repository;

    public List<Nabavka> findAll() {
        return repository.findAll();
    }

    public Nabavka findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nabavka nije pronađena"));
    }

    public Nabavka save(Nabavka nabavka) {
        nabavka.setDatum(Instant.now());
        return repository.save(nabavka);
    }

    public void delete(Integer id) {
        findById(id);
        repository.deleteById(id);
    }
}
