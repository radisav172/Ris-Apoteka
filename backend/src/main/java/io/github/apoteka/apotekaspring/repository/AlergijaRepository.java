package io.github.apoteka.apotekaspring.repository;

import io.github.apoteka.apotekaspring.model.Alergija;
import io.github.apoteka.apotekaspring.model.AlergijaId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlergijaRepository extends JpaRepository<Alergija, AlergijaId> {
    List<Alergija> findByIdPacijentId(Integer pacijentId);
}
