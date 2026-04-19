package io.github.apoteka.apotekaspring.service;

import io.github.apoteka.apotekaspring.model.Kategorijaleka;
import io.github.apoteka.apotekaspring.repository.KategorijaLekaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KategorijaLekaService {

    private final KategorijaLekaRepository repository;

    public List<Kategorijaleka> findAll() {
        return repository.findAll();
    }
}
