package io.github.apoteka.apotekaspring.controller;

import io.github.apoteka.apotekaspring.model.Nabavka;
import io.github.apoteka.apotekaspring.service.NabavkaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/nabavke")
@RequiredArgsConstructor
public class NabavkaController {

    private final NabavkaService service;

    @GetMapping
    public ResponseEntity<List<Nabavka>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Nabavka> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Nabavka> create(@Valid @RequestBody Nabavka nabavka) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(nabavka));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
