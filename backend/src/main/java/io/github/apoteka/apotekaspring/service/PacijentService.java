package io.github.apoteka.apotekaspring.service;

import io.github.apoteka.apotekaspring.model.Alergija;
import io.github.apoteka.apotekaspring.model.Lek;
import io.github.apoteka.apotekaspring.model.Pacijent;
import io.github.apoteka.apotekaspring.model.Prodaja;
import io.github.apoteka.apotekaspring.repository.AlergijaRepository;
import io.github.apoteka.apotekaspring.repository.LekRepository;
import io.github.apoteka.apotekaspring.repository.PacijentRepository;
import io.github.apoteka.apotekaspring.repository.ProdajaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PacijentService {

    private final PacijentRepository pacijentRepository;
    private final AlergijaRepository alergijaRepository;
    private final LekRepository lekRepository;
    private final ProdajaRepository prodajaRepository;

    public List<Pacijent> getAll() {
        return pacijentRepository.findAll();
    }

    public Pacijent getById(Integer id) {
        return pacijentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pacijent nije pronađen"));
    }

    public List<Lek> getAlergije(Integer id) {
        getById(id);
        return alergijaRepository.findByIdPacijentId(id)
                .stream()
                .map(Alergija::getLek)
                .collect(Collectors.toList());
    }

    public List<Prodaja> getIstorija(Integer id) {
        getById(id);
        return prodajaRepository.findByPacijentId(id);
    }

    public Pacijent create(Pacijent pacijent) {
        if (pacijentRepository.existsByJmbg(pacijent.getJmbg())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Pacijent sa tim JMBG već postoji");
        }
        return pacijentRepository.save(pacijent);
    }

    public Pacijent update(Integer id, Pacijent pacijent) {
        Pacijent existing = getById(id);
        existing.setIme(pacijent.getIme());
        existing.setPrezime(pacijent.getPrezime());
        existing.setJmbg(pacijent.getJmbg());
        existing.setDatumRodjenja(pacijent.getDatumRodjenja());
        existing.setKontakt(pacijent.getKontakt());
        return pacijentRepository.save(existing);
    }

    public void delete(Integer id) {
        getById(id);
        pacijentRepository.deleteById(id);
    }

    public void addAlergija(Integer pacijentId, Integer lekId) {
        Pacijent pacijent = getById(pacijentId);
        Lek lek = lekRepository.findById(lekId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Lek nije pronađen"));

        AlergijaId alergijaId = new AlergijaId();
        alergijaId.setPacijentId(pacijentId);
        alergijaId.setLekId(lekId);

        if (alergijaRepository.existsById(alergijaId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Alergija već postoji");
        }

        Alergija alergija = new Alergija();
        alergija.setId(alergijaId);
        alergija.setPacijent(pacijent);
        alergija.setLek(lek);
        alergijaRepository.save(alergija);
    }

    public void removeAlergija(Integer pacijentId, Integer lekId) {
        AlergijaId alergijaId = new AlergijaId();
        alergijaId.setPacijentId(pacijentId);
        alergijaId.setLekId(lekId);

        if (!alergijaRepository.existsById(alergijaId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Alergija nije pronađena");
        }
        alergijaRepository.deleteById(alergijaId);
    }
}
