package io.github.apoteka.apotekaspring.controller;

import io.github.apoteka.apotekaspring.model.Lek;
import io.github.apoteka.apotekaspring.service.LekService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/lekovi")
@RequiredArgsConstructor
public class LekController {

    private final LekService service;

    @GetMapping
    public ResponseEntity<List<Lek>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/niske-zalihe")
    public ResponseEntity<List<Lek>> getNiskeZalihe() {
        return ResponseEntity.ok(service.findNiskeZalihe());
    }

    @GetMapping("/isticu-rok")
    public ResponseEntity<List<Lek>> getIsticuRok(@RequestParam(defaultValue = "30") int danaDo) {
        return ResponseEntity.ok(service.findIsticuRok(danaDo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lek> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Lek> create(@Valid @RequestBody Lek lek) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(lek));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Lek> update(@PathVariable Integer id, @Valid @RequestBody Lek lek) {
        return ResponseEntity.ok(service.update(id, lek));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
