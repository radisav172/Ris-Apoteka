package io.github.apoteka.apotekaspring.controller;

import io.github.apoteka.apotekaspring.model.Pacijent;
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
}
