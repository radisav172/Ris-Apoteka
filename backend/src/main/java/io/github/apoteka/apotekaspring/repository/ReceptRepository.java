package io.github.apoteka.apotekaspring.repository;

import io.github.apoteka.apotekaspring.model.Recept;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceptRepository extends JpaRepository<Recept, Integer> {
}
