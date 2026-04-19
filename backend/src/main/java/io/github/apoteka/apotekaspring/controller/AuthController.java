package io.github.apoteka.apotekaspring.controller;

import io.github.apoteka.apotekaspring.dto.LoginRequest;
import io.github.apoteka.apotekaspring.dto.LoginResponse;
import io.github.apoteka.apotekaspring.dto.RegisterRequest;
import io.github.apoteka.apotekaspring.model.Korisnik;
import io.github.apoteka.apotekaspring.model.RolaTip;
import io.github.apoteka.apotekaspring.repository.KorisnikRepository;
import io.github.apoteka.apotekaspring.service.KorisnikService;
import io.github.apoteka.apotekaspring.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final KorisnikRepository korisnikRepository;
    private final KorisnikService korisnikService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @GetMapping("/me")
    public ResponseEntity<Korisnik> me(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        String korisnickoIme = jwtUtil.extractKorisnickoIme(token);
        Korisnik korisnik = korisnikRepository.findByKorisnickoIme(korisnickoIme)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Korisnik nije pronađen"));
        return ResponseEntity.ok(korisnik);
    }

    @PostMapping("/register")
    public ResponseEntity<LoginResponse> register(@RequestBody RegisterRequest request) {
        Korisnik korisnik = new Korisnik();
        korisnik.setIme(request.getIme());
        korisnik.setPrezime(request.getPrezime());
        korisnik.setKorisnickoIme(request.getKorisnickoIme());
        korisnik.setLozinkaHash(request.getLozinka());
        korisnik.setRola(request.getRola() != null ? RolaTip.valueOf(request.getRola()) : RolaTip.korisnik);

        Korisnik saved = korisnikService.create(korisnik);
        String token = jwtUtil.generate(saved.getKorisnickoIme(), saved.getRola().name());
        return ResponseEntity.status(HttpStatus.CREATED).body(new LoginResponse(token, saved.getRola().name()));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        Korisnik korisnik = korisnikRepository.findByKorisnickoIme(request.getKorisnickoIme())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Pogrešno korisničko ime ili lozinka"));

        if (!passwordEncoder.matches(request.getLozinka(), korisnik.getLozinkaHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Pogrešno korisničko ime ili lozinka");
        }

        String token = jwtUtil.generate(korisnik.getKorisnickoIme(), korisnik.getRola().name());
        return ResponseEntity.ok(new LoginResponse(token, korisnik.getRola().name()));
    }
}
