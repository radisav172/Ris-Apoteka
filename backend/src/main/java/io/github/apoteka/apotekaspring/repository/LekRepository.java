package io.github.apoteka.apotekaspring.repository;

import io.github.apoteka.apotekaspring.model.Lek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LekRepository extends JpaRepository<Lek, Integer> {

    @Query("SELECT l FROM Lek l WHERE l.kolicinaNaStanju <= l.minimalnaZaliha")
    List<Lek> findNiskeZalihe();

    @Query("SELECT l FROM Lek l WHERE l.rokTrajanja IS NOT NULL AND l.rokTrajanja BETWEEN :danas AND :granica")
    List<Lek> findIsticuRok(@Param("danas") LocalDate danas, @Param("granica") LocalDate granica);
}
