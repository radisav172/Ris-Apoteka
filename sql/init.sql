CREATE TABLE KategorijaLeka (
    id     SERIAL PRIMARY KEY,
    naziv  VARCHAR(100) NOT NULL,
    opis   TEXT
);

CREATE TABLE Lek (
    id                 SERIAL PRIMARY KEY,
    naziv              VARCHAR(150)   NOT NULL,
    sifra              VARCHAR(50)    NOT NULL UNIQUE,
    proizvodjac        VARCHAR(100)   NOT NULL,
    oblik              VARCHAR(100),
    jacina_doze        VARCHAR(50),
    kolicina_na_stanju INT            NOT NULL DEFAULT 0,
    minimalna_zaliha   INT            NOT NULL DEFAULT 0,
    rok_trajanja       DATE,
    cena               NUMERIC(10, 2) NOT NULL,
    na_recept          BOOLEAN        NOT NULL DEFAULT FALSE,
    kategorija_id      INT REFERENCES KategorijaLeka (id) ON DELETE SET NULL
);

CREATE TABLE Dobavljac (
    id      SERIAL PRIMARY KEY,
    naziv   VARCHAR(150) NOT NULL,
    adresa  VARCHAR(255),
    kontakt VARCHAR(100),
    pib     VARCHAR(20)  NOT NULL UNIQUE
);

CREATE TABLE Lek_Dobavljac (
    lek_id       INT NOT NULL REFERENCES Lek       (id) ON DELETE CASCADE,
    dobavljac_id INT NOT NULL REFERENCES Dobavljac (id) ON DELETE CASCADE,
    PRIMARY KEY (lek_id, dobavljac_id)
);

CREATE TABLE Pacijent (
    id             SERIAL PRIMARY KEY,
    ime            VARCHAR(100) NOT NULL,
    prezime        VARCHAR(100) NOT NULL,
    jmbg           VARCHAR(13)  NOT NULL UNIQUE,
    datum_rodjenja DATE,
    kontakt        VARCHAR(100)
);

CREATE TABLE Alergija (
    pacijent_id INT NOT NULL REFERENCES Pacijent (id) ON DELETE CASCADE,
    lek_id      INT NOT NULL REFERENCES Lek      (id) ON DELETE CASCADE,
    PRIMARY KEY (pacijent_id, lek_id)
);

CREATE TYPE rola_tip AS ENUM ('admin', 'korisnik');

CREATE TABLE Korisnik (
    id             SERIAL PRIMARY KEY,
    ime            VARCHAR(100) NOT NULL,
    prezime        VARCHAR(100) NOT NULL,
    korisnicko_ime VARCHAR(50)  NOT NULL UNIQUE,
    lozinka_hash   VARCHAR(255) NOT NULL,
    rola           rola_tip     NOT NULL DEFAULT 'korisnik'
);

CREATE TYPE recept_status AS ENUM ('aktivan', 'iskoriscen', 'istekao');

CREATE TABLE Recept (
    id           SERIAL PRIMARY KEY,
    broj_recepta VARCHAR(50)   NOT NULL UNIQUE,
    datum        DATE          NOT NULL,
    status       recept_status NOT NULL DEFAULT 'aktivan',
    pacijent_id  INT           NOT NULL REFERENCES Pacijent  (id) ON DELETE RESTRICT,
    korisnik_id INT           NOT NULL REFERENCES Korisnik (id) ON DELETE RESTRICT
);

CREATE TABLE Prodaja (
    id           SERIAL PRIMARY KEY,
    datum        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    napomena     TEXT,
    korisnik_id INT       NOT NULL REFERENCES Korisnik (id) ON DELETE RESTRICT,
    pacijent_id  INT                REFERENCES Pacijent  (id) ON DELETE SET NULL,
    recept_id    INT       UNIQUE   REFERENCES Recept    (id) ON DELETE SET NULL
);

CREATE TABLE StavkaProdaje (
    id           SERIAL PRIMARY KEY,
    kolicina     INT            NOT NULL,
    cena_prodaje NUMERIC(10, 2) NOT NULL,
    prodaja_id   INT            NOT NULL REFERENCES Prodaja (id) ON DELETE CASCADE,
    lek_id       INT            NOT NULL REFERENCES Lek     (id) ON DELETE RESTRICT
);

CREATE TABLE Nabavka (
    id           SERIAL PRIMARY KEY,
    datum        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    napomena     TEXT,
    dobavljac_id INT       NOT NULL REFERENCES Dobavljac (id) ON DELETE RESTRICT
);

CREATE TABLE StavkaNabavke (
    id           SERIAL PRIMARY KEY,
    kolicina     INT            NOT NULL,
    nabavna_cena NUMERIC(10, 2) NOT NULL,
    nabavka_id   INT            NOT NULL REFERENCES Nabavka (id) ON DELETE CASCADE,
    lek_id       INT            NOT NULL REFERENCES Lek     (id) ON DELETE RESTRICT
);
