package io.github.apoteka.apotekaspring.controller;

import io.github.apoteka.apotekaspring.model.Dobavljac;
import io.github.apoteka.apotekaspring.service.DobavljacService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/dobavljaci")
@RequiredArgsConstructor
public class DobavljacController {

    private final DobavljacService service;

    @GetMapping
    public ResponseEntity<List<Dobavljac>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Dobavljac> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Dobavljac> create(@Valid @RequestBody Dobavljac dobavljac) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(dobavljac));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Dobavljac> update(@PathVariable Integer id, @Valid @RequestBody Dobavljac dobavljac) {
        return ResponseEntity.ok(service.update(id, dobavljac));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
