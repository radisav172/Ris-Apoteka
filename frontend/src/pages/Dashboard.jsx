import Widget from '../components/Widget'
import styles from './Dashboard.module.css'

const widgets = [
  {
    title: 'Lekovi', endpoint: '/lekovi', to: '/lekovi',
    columns: [
      { key: 'naziv', label: 'Naziv' },
      { key: 'kolicinaNaStanju', label: 'Zaliha' },
      { key: 'cena', label: 'Cena (RSD)' },
    ],
  },
  {
    title: 'Pacijenti', endpoint: '/pacijenti', to: '/pacijenti',
    columns: [
      { key: 'ime', label: 'Ime', render: r => `${r.ime} ${r.prezime}` },
      { key: 'kontakt', label: 'Kontakt' },
    ],
  },
  {
    title: 'Prodaje', endpoint: '/prodaje', to: '/prodaje',
    columns: [
      { key: 'datum', label: 'Datum', render: r => r.datum?.slice(0, 10) },
      { key: 'korisnik', label: 'Korisnik', render: r => r.korisnik ? `${r.korisnik.ime} ${r.korisnik.prezime}` : '-' },
    ],
  },
  {
    title: 'Nabavke', endpoint: '/nabavke', to: '/nabavke',
    columns: [
      { key: 'datum', label: 'Datum', render: r => r.datum?.slice(0, 10) },
      { key: 'dobavljac', label: 'Dobavljac', render: r => r.dobavljac?.naziv ?? '-' },
    ],
  },
  {
    title: 'Recepti', endpoint: '/recepti', to: '/recepti',
    columns: [
      { key: 'brojRecepta', label: 'Broj' },
      { key: 'status', label: 'Status' },
    ],
  },
  {
    title: 'Dobavljaci', endpoint: '/dobavljaci', to: '/dobavljaci',
    columns: [
      { key: 'naziv', label: 'Naziv' },
      { key: 'kontakt', label: 'Kontakt' },
    ],
  },
  {
    title: 'Kategorije', endpoint: '/kategorije', to: '/kategorije',
    columns: [
      { key: 'naziv', label: 'Naziv' },
      { key: 'opis', label: 'Opis' },
    ],
  },
  {
    title: 'Korisnici', endpoint: '/korisnici', to: '/korisnici',
    columns: [
      { key: 'korisnickoIme', label: 'Korisnicko ime' },
      { key: 'rola', label: 'Rola' },
    ],
  },
]

export default function Dashboard() {
  return (
    <div>
      <h2 className={styles.heading}>Dashboard</h2>
      <div className={styles.grid}>
        {widgets.map(w => <Widget key={w.to} {...w} />)}
      </div>
    </div>
  )
}
