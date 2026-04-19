package io.github.apoteka.apotekaspring.controller;

import io.github.apoteka.apotekaspring.model.Prodaja;
import io.github.apoteka.apotekaspring.service.ProdajaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/prodaje")
@RequiredArgsConstructor
public class ProdajaController {

    private final ProdajaService service;

    @GetMapping
    public ResponseEntity<List<Prodaja>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/period")
    public ResponseEntity<List<Prodaja>> getByPeriod(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate od,
            @RequestParam("do") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate do_) {
        return ResponseEntity.ok(service.findByPeriod(od, do_));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prodaja> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<Prodaja> create(@Valid @RequestBody Prodaja prodaja) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(prodaja));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
