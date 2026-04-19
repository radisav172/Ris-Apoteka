package io.github.apoteka.apotekaspring.controller;

import io.github.apoteka.apotekaspring.service.IzvestajiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/izvestaji")
@RequiredArgsConstructor
public class IzvestajiController {

    private final IzvestajiService service;

    @GetMapping("/racun/{prodajaId}")
    public ResponseEntity<byte[]> getRacun(@PathVariable Integer prodajaId) {
        byte[] pdf = service.generisiRacun(prodajaId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"racun-" + prodajaId + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/karton/{pacijentId}")
    public ResponseEntity<byte[]> getKarton(@PathVariable Integer pacijentId) {
        byte[] pdf = service.generisiKarton(pacijentId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"karton-" + pacijentId + ".pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @GetMapping("/zalihe")
    public ResponseEntity<byte[]> getZalihe() {
        byte[] pdf = service.generisiZalihe();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"zalihe.pdf\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
