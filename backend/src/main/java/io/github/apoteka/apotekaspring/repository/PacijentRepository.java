package io.github.apoteka.apotekaspring.repository;

import io.github.apoteka.apotekaspring.model.Pacijent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacijentRepository extends JpaRepository<Pacijent, Integer> {
    boolean existsByJmbg(String jmbg);
}
