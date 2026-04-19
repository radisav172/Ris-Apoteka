package io.github.apoteka.apotekaspring.repository;

import io.github.apoteka.apotekaspring.model.Prodaja;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProdajaRepository extends JpaRepository<Prodaja, Integer> {
    List<Prodaja> findByPacijentId(Integer pacijentId);
}
