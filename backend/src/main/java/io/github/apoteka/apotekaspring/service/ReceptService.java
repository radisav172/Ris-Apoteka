package io.github.apoteka.apotekaspring.service;

import io.github.apoteka.apotekaspring.model.Recept;
import io.github.apoteka.apotekaspring.model.ReceptStatus;
import io.github.apoteka.apotekaspring.repository.ReceptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReceptService {

    private final ReceptRepository repository;

    public List<Recept> findAll() {
        return repository.findAll();
    }

    public Recept findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Recept nije pronađen"));
    }

    public Recept save(Recept recept) {
        return repository.save(recept);
    }

    public Recept iskoristi(Integer id) {
        Recept recept = findById(id);
        if (recept.getStatus() != ReceptStatus.aktivan) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Recept nije aktivan");
        }
        recept.setStatus(ReceptStatus.iskoriscen);
        return repository.save(recept);
    }
}
