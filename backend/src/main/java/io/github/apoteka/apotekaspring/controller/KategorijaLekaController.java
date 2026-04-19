package io.github.apoteka.apotekaspring.controller;

import io.github.apoteka.apotekaspring.model.Kategorijaleka;
import io.github.apoteka.apotekaspring.service.KategorijaLekaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
