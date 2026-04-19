package io.github.apoteka.apotekaspring.service;

import io.github.apoteka.apotekaspring.dto.KartonStavkaDto;
import io.github.apoteka.apotekaspring.dto.RacunStavkaDto;
import io.github.apoteka.apotekaspring.dto.ZaliheStavkaDto;
import io.github.apoteka.apotekaspring.model.Alergija;
import io.github.apoteka.apotekaspring.model.Lek;
import io.github.apoteka.apotekaspring.model.Pacijent;
import io.github.apoteka.apotekaspring.model.Prodaja;
import io.github.apoteka.apotekaspring.model.Stavkaprodaje;
import io.github.apoteka.apotekaspring.repository.AlergijaRepository;
import io.github.apoteka.apotekaspring.repository.LekRepository;
import io.github.apoteka.apotekaspring.repository.PacijentRepository;
import io.github.apoteka.apotekaspring.repository.ProdajaRepository;
import io.github.apoteka.apotekaspring.repository.StavkaProdajeRepository;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IzvestajiService {

    private final ProdajaService prodajaService;
    private final ProdajaRepository prodajaRepository;
    private final StavkaProdajeRepository stavkaProdajeRepository;
    private final PacijentRepository pacijentRepository;
    private final AlergijaRepository alergijaRepository;
    private final LekRepository lekRepository;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public byte[] generisiRacun(Integer prodajaId) {
        Prodaja prodaja = prodajaService.findById(prodajaId);
        List<Stavkaprodaje> stavke = stavkaProdajeRepository.findByProdajaId(prodajaId);

        if (stavke.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Prodaja nema stavki");
        }

        List<RacunStavkaDto> redovi = stavke.stream().map(s -> new RacunStavkaDto(
                s.getLek().getNaziv(),
                s.getKolicina(),
                s.getCenaProdaje(),
                s.getCenaProdaje().multiply(BigDecimal.valueOf(s.getKolicina()))
        )).toList();

        BigDecimal ukupno = redovi.stream()
                .map(RacunStavkaDto::getUkupno)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String datum = prodaja.getDatum().atZone(ZoneId.systemDefault()).format(FORMATTER);
        String korisnik = prodaja.getKorisnik().getIme() + " " + prodaja.getKorisnik().getPrezime();
        String pacijent = prodaja.getPacijent() != null
                ? prodaja.getPacijent().getIme() + " " + prodaja.getPacijent().getPrezime()
                : null;

        Map<String, Object> parametri = new HashMap<>();
        parametri.put("prodajaId", prodajaId);
        parametri.put("datum", datum);
        parametri.put("korisnik", korisnik);
        parametri.put("pacijent", pacijent);
        parametri.put("ukupnoIznos", ukupno);

        return generisiPdf("/reports/racun.jrxml", parametri, redovi);
    }

    public byte[] generisiKarton(Integer pacijentId) {
        Pacijent pacijent = pacijentRepository.findById(pacijentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pacijent nije pronadjen"));

        List<Alergija> alergije = alergijaRepository.findByIdPacijentId(pacijentId);
        String alergijeStr = alergije.isEmpty() ? "Nema evidentiranih alergija"
                : alergije.stream().map(a -> a.getLek().getNaziv()).collect(Collectors.joining(", "));

        List<Prodaja> prodaje = prodajaRepository.findByPacijentId(pacijentId);
        List<KartonStavkaDto> redovi = prodaje.stream()
                .flatMap(p -> stavkaProdajeRepository.findByProdajaId(p.getId()).stream()
                        .map(s -> new KartonStavkaDto(
                                p.getDatum().atZone(ZoneId.systemDefault()).format(FORMATTER),
                                s.getLek().getNaziv(),
                                s.getKolicina(),
                                s.getCenaProdaje()
                        )))
                .toList();

        Map<String, Object> parametri = new HashMap<>();
        parametri.put("ime", pacijent.getIme());
        parametri.put("prezime", pacijent.getPrezime());
        parametri.put("jmbg", pacijent.getJmbg());
        parametri.put("datumRodjenja", pacijent.getDatumRodjenja() != null
                ? pacijent.getDatumRodjenja().toString() : null);
        parametri.put("kontakt", pacijent.getKontakt());
        parametri.put("alergije", alergijeStr);

        return generisiPdf("/reports/karton.jrxml", parametri, redovi);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public byte[] generisiZalihe() {
        List<Lek> lekovi = lekRepository.findAll();

        List<ZaliheStavkaDto> redovi = lekovi.stream().map(l -> new ZaliheStavkaDto(
                l.getNaziv(),
                l.getSifra(),
                l.getKategorija() != null ? l.getKategorija().getNaziv() : null,
                l.getKolicinaNaStanju(),
                l.getMinimalnaZaliha(),
                l.getCena(),
                l.getRokTrajanja() != null ? l.getRokTrajanja().toString() : "-",
                l.getKolicinaNaStanju() <= l.getMinimalnaZaliha()
        )).toList();

        String datum = java.time.LocalDateTime.now().format(FORMATTER);

        Map<String, Object> parametri = new HashMap<>();
        parametri.put("datum", datum);

        return generisiPdf("/reports/zalihe.jrxml", parametri, redovi);
    }

    private byte[] generisiPdf(String jrxmlPath, Map<String, Object> parametri, List<?> redovi) {
        try {
            InputStream jrxml = getClass().getResourceAsStream(jrxmlPath);
            JasperReport jasperReport = JasperCompileManager.compileReport(jrxml);
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport, parametri, new JRBeanCollectionDataSource(redovi));
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (JRException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Greska pri generisanju izvestaja");
        }
    }
}
