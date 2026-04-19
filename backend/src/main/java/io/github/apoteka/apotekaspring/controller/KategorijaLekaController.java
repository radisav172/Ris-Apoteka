package io.github.apoteka.apotekaspring.controller;

import io.github.apoteka.apotekaspring.model.Kategorijaleka;
import io.github.apoteka.apotekaspring.service.KategorijaLekaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/kategorije")
@RequiredArgsConstructor
public class KategorijaLekaController {

    private final KategorijaLekaService service;

    @GetMapping
    public ResponseEntity<List<Kategorijaleka>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Kategorijaleka> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Kategorijaleka> create(@Valid @RequestBody Kategorijaleka kategorija) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(kategorija));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Kategorijaleka> update(@PathVariable Integer id, @Valid @RequestBody Kategorijaleka kategorija) {
        return ResponseEntity.ok(service.update(id, kategorija));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
