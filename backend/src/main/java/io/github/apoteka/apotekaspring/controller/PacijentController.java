package io.github.apoteka.apotekaspring.controller;

import io.github.apoteka.apotekaspring.model.Lek;
import io.github.apoteka.apotekaspring.model.Pacijent;
import io.github.apoteka.apotekaspring.model.Prodaja;
import io.github.apoteka.apotekaspring.service.PacijentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacijenti")
@RequiredArgsConstructor
public class PacijentController {

    private final PacijentService pacijentService;

    @GetMapping
    public ResponseEntity<List<Pacijent>> getAll() {
        return ResponseEntity.ok(pacijentService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pacijent> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(pacijentService.getById(id));
    }

    @GetMapping("/{id}/alergije")
    public ResponseEntity<List<Lek>> getAlergije(@PathVariable Integer id) {
        return ResponseEntity.ok(pacijentService.getAlergije(id));
    }

    @GetMapping("/{id}/istorija")
    public ResponseEntity<List<Prodaja>> getIstorija(@PathVariable Integer id) {
        return ResponseEntity.ok(pacijentService.getIstorija(id));
    }

    @PostMapping
    public ResponseEntity<Pacijent> create(@RequestBody Pacijent pacijent) {
        return ResponseEntity.status(201).body(pacijentService.create(pacijent));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pacijent> update(@PathVariable Integer id, @RequestBody Pacijent pacijent) {
        return ResponseEntity.ok(pacijentService.update(id, pacijent));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        pacijentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/alergije/{lekId}")
    public ResponseEntity<Void> addAlergija(@PathVariable Integer id, @PathVariable Integer lekId) {
        pacijentService.addAlergija(id, lekId);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/{id}/alergije/{lekId}")
    public ResponseEntity<Void> removeAlergija(@PathVariable Integer id, @PathVariable Integer lekId) {
        pacijentService.removeAlergija(id, lekId);
        return ResponseEntity.noContent().build();
    }
}
