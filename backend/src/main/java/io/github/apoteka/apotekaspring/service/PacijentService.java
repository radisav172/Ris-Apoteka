package io.github.apoteka.apotekaspring.service;

import io.github.apoteka.apotekaspring.model.Pacijent;
import io.github.apoteka.apotekaspring.repository.PacijentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PacijentService {

    private final PacijentRepository pacijentRepository;

    public List<Pacijent> getAll() {
        return pacijentRepository.findAll();
    }
}
