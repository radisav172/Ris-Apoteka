package io.github.apoteka.apotekaspring.repository;

import io.github.apoteka.apotekaspring.model.Stavkaprodaje;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StavkaProdajeRepository extends JpaRepository<Stavkaprodaje, Integer> {
    List<Stavkaprodaje> findByProdajaId(Integer prodajaId);
}
