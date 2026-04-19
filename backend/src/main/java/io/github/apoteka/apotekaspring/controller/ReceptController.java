package io.github.apoteka.apotekaspring.controller;

import io.github.apoteka.apotekaspring.model.Recept;
import io.github.apoteka.apotekaspring.service.ReceptService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/recepti")
@RequiredArgsConstructor
public class ReceptController {

    private final ReceptService service;

    @GetMapping
    public ResponseEntity<List<Recept>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recept> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Recept> create(@Valid @RequestBody Recept recept) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(recept));
    }

    @PutMapping("/{id}/iskoristi")
    public ResponseEntity<Recept> iskoristi(@PathVariable Integer id) {
        return ResponseEntity.ok(service.iskoristi(id));
    }
}
