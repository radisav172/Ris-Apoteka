package io.github.apoteka.apotekaspring.service;

import io.github.apoteka.apotekaspring.model.Prodaja;
import io.github.apoteka.apotekaspring.repository.ProdajaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdajaService {

    private final ProdajaRepository repository;

    public List<Prodaja> findAll() {
        return repository.findAll();
    }

    public Prodaja findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prodaja nije pronađena"));
    }

    public List<Prodaja> findByPeriod(LocalDate od, LocalDate do_) {
        return repository.findByDatumBetween(
                od.atStartOfDay().toInstant(ZoneOffset.UTC),
                do_.plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC)
        );
    }

    public Prodaja save(Prodaja prodaja) {
        prodaja.setDatum(Instant.now());
        return repository.save(prodaja);
    }

    public void delete(Integer id) {
        findById(id);
        repository.deleteById(id);
    }
}
