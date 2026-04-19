package io.github.apoteka.apotekaspring.controller;

import io.github.apoteka.apotekaspring.model.Korisnik;
import io.github.apoteka.apotekaspring.service.KorisnikService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/korisnici")
@RequiredArgsConstructor
public class KorisnikController {

    private final KorisnikService korisnikService;

    @GetMapping
    public ResponseEntity<List<Korisnik>> getAll() {
        return ResponseEntity.ok(korisnikService.getAll());
    }

    @PostMapping
    public ResponseEntity<Korisnik> create(@RequestBody Korisnik korisnik) {
        return ResponseEntity.status(201).body(korisnikService.create(korisnik));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Korisnik> update(@PathVariable Integer id, @RequestBody Korisnik korisnik) {
        return ResponseEntity.ok(korisnikService.update(id, korisnik));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        korisnikService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
