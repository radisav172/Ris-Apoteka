-- Kategorije lekova
INSERT INTO kategorijaleka (naziv, opis) VALUES
('Antibiotici', 'Lekovi za lečenje bakterijskih infekcija'),
('Analgetici', 'Lekovi za ublažavanje bola'),
('Antihistaminici', 'Lekovi za alergije'),
('Kardiovaskularni', 'Lekovi za srce i krvne sudove'),
('Vitamini i suplementi', 'Vitamini, minerali i dodaci ishrani');

-- Lekovi
INSERT INTO lek (naziv, sifra, proizvodjac, oblik, jacina_doze, kolicina_na_stanju, minimalna_zaliha, rok_trajanja, cena, na_recept, kategorija_id) VALUES
('Amoksicilin', 'AMO500', 'Hemofarm', 'Kapsula', '500mg', 120, 20, '2026-08-01', 850.00, true, 1),
('Brufen', 'BRU400', 'Abbott', 'Tableta', '400mg', 200, 30, '2026-12-01', 420.00, false, 2),
('Paracetamol', 'PAR500', 'Galenika', 'Tableta', '500mg', 350, 50, '2027-03-01', 180.00, false, 2),
('Cetirizin', 'CET10', 'Hemofarm', 'Tableta', '10mg', 90, 15, '2026-10-01', 310.00, false, 3),
('Atorvastatin', 'ATO20', 'Pfizer', 'Tableta', '20mg', 75, 10, '2026-09-01', 1200.00, true, 4),
('Vitamin C', 'VITC500', 'Bayer', 'Tableta', '500mg', 500, 100, '2027-06-01', 250.00, false, 5);

-- Dobavljaci
INSERT INTO dobavljac (naziv, adresa, kontakt, pib) VALUES
('Farmalogist d.o.o.', 'Bulevar Oslobođenja 12, Novi Sad', '+381 21 555 111', '101234567'),
('MediSupply Serbia', 'Knez Mihailova 5, Beograd', '+381 11 333 222', '102345678'),
('PharmaDis', 'Cara Dušana 88, Niš', '+381 18 444 333', '103456789');

-- Lek - Dobavljac veze
INSERT INTO lek_dobavljac (lek_id, dobavljac_id) VALUES
(1, 1), (1, 2),
(2, 2), (2, 3),
(3, 1), (3, 3),
(4, 2),
(5, 1), (5, 2),
(6, 3);

-- Pacijenti
INSERT INTO pacijent (ime, prezime, jmbg, datum_rodjenja, kontakt) VALUES
('Marko', 'Marković', '0101990710123', '1990-01-01', '+381 64 111 2233'),
('Ana', 'Anić', '1505985755012', '1985-05-15', '+381 65 222 3344'),
('Petar', 'Petrović', '2209978710045', '1978-09-22', '+381 66 333 4455'),
('Jelena', 'Jovanović', '0803992715067', '1992-03-08', '+381 63 444 5566'),
('Nikola', 'Nikolić', '1412000710089', '2000-12-14', '+381 64 555 6677');

-- Alergije
INSERT INTO alergija (pacijent_id, lek_id) VALUES
(1, 1),
(2, 4),
(3, 1), (3, 2),
(5, 3);

-- Korisnici
INSERT INTO korisnik (ime, prezime, korisnicko_ime, lozinka_hash, rola) VALUES
-- lozinke: admin123, korisnik123, korisnik123
('Stefan', 'Stefanović', 'admin', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'admin'),
('Milica', 'Milić', 'milicam', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'korisnik'),
('Dragan', 'Dragić', 'dragand', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2uheWG/igi.', 'korisnik');

-- Recepti
INSERT INTO recept (broj_recepta, datum, status, pacijent_id, korisnik_id) VALUES
('REC-2024-001', '2024-11-10', 'iskoriscen', 1, 2),
('REC-2024-002', '2024-12-05', 'aktivan', 3, 2),
('REC-2025-001', '2025-01-15', 'aktivan', 2, 3);

-- Prodaje
INSERT INTO prodaja (datum, napomena, korisnik_id, pacijent_id, recept_id) VALUES
('2024-11-10 10:30:00', NULL, 2, 1, 1),
('2024-12-20 14:00:00', 'Placeno gotovinom', 3, 3, NULL),
('2025-01-16 09:15:00', NULL, 2, 4, NULL);

-- Stavke prodaje
INSERT INTO stavkaprodaje (kolicina, cena_prodaje, prodaja_id, lek_id) VALUES
(1, 850.00, 1, 1),
(2, 180.00, 1, 3),
(1, 420.00, 2, 2),
(3, 250.00, 2, 6),
(1, 310.00, 3, 4),
(1, 180.00, 3, 3);

-- Nabavke
INSERT INTO nabavka (datum, napomena, dobavljac_id) VALUES
('2024-10-01 08:00:00', 'Redovna mesečna nabavka', 1),
('2024-11-01 08:00:00', 'Redovna mesečna nabavka', 2),
('2024-12-01 08:00:00', 'Urgentna nabavka', 3);

-- Stavke nabavke
INSERT INTO stavkanabavke (kolicina, nabavna_cena, nabavka_id, lek_id) VALUES
(100, 700.00, 1, 1),
(200, 130.00, 1, 3),
(150, 300.00, 2, 2),
(100, 220.00, 2, 4),
(50,  950.00, 2, 5),
(300, 190.00, 3, 6);

INSERT INTO Dobavljac (naziv, adresa, kontakt, pib) VALUES
('Hemofarm d.o.o.',  'Vršac, Beogradski put bb', '013/800-100', '123456789'),
('Galenika a.d.',    'Zemun, Batajnički drum 2', '011/307-5000', '987654321'),
('Bayer d.o.o.',     'Beograd, Omladinskih brigada 88', '011/220-0100', '111222333');
