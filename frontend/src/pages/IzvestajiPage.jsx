import { useEffect, useState } from 'react'
import { api, downloadPdf } from '../api'
import styles from './IzvestajiPage.module.css'

export default function IzvestajiPage() {
  const [pacijenti, setPacijenti] = useState([])
  const [prodaje, setProdaje] = useState([])
  const [pacijentId, setPacijentId] = useState('')
  const [prodajaId, setProdajaId] = useState('')
  const [loading, setLoading] = useState({})

  useEffect(() => {
    api.get('/pacijenti').then(setPacijenti)
    api.get('/prodaje').then(setProdaje)
  }, [])

  async function preuzmi(key, path, filename) {
    setLoading(l => ({ ...l, [key]: true }))
    try {
      await downloadPdf(path, filename)
    } catch {
      alert('Greska pri preuzimanju izvestaja')
    } finally {
      setLoading(l => ({ ...l, [key]: false }))
    }
  }

  return (
    <div>
      <h2 className={styles.heading}>Izvestaji</h2>
      <div className={styles.grid}>

        <div className={styles.card}>
          <div className={styles.cardTitle}>Izvestaj o zalihama</div>
          <p className={styles.cardDesc}>Pregled svih lekova sa trenutnim stanjem zaliha, minimalnim zalihama i rokom trajanja.</p>
          <button
            className={styles.btn}
            disabled={loading.zalihe}
            onClick={() => preuzmi('zalihe', '/izvestaji/zalihe', 'zalihe.pdf')}
          >
            {loading.zalihe ? 'Preuzimanje...' : 'Preuzmi PDF'}
          </button>
        </div>

        <div className={styles.card}>
          <div className={styles.cardTitle}>Racun za prodaju</div>
          <p className={styles.cardDesc}>Generisi racun za odabranu prodaju sa listom stavki i ukupnim iznosom.</p>
          <div className={styles.row}>
            <select value={prodajaId} onChange={e => setProdajaId(e.target.value)} className={styles.select}>
              <option value="">Izaberi prodaju...</option>
              {prodaje.map(p => (
                <option key={p.id} value={p.id}>
                  #{p.id} — {p.datum?.slice(0, 10)} — {p.korisnik ? `${p.korisnik.ime} ${p.korisnik.prezime}` : '-'}
                </option>
              ))}
            </select>
            <button
              className={styles.btn}
              disabled={!prodajaId || loading.racun}
              onClick={() => preuzmi('racun', `/izvestaji/racun/${prodajaId}`, `racun-${prodajaId}.pdf`)}
            >
              {loading.racun ? 'Preuzimanje...' : 'Preuzmi PDF'}
            </button>
          </div>
        </div>

        <div className={styles.card}>
          <div className={styles.cardTitle}>Karton pacijenta</div>
          <p className={styles.cardDesc}>Karton sa licnim podacima, alergijama i istorijom kupovina pacijenta.</p>
          <div className={styles.row}>
            <select value={pacijentId} onChange={e => setPacijentId(e.target.value)} className={styles.select}>
              <option value="">Izaberi pacijenta...</option>
              {pacijenti.map(p => (
                <option key={p.id} value={p.id}>{p.ime} {p.prezime}</option>
              ))}
            </select>
            <button
              className={styles.btn}
              disabled={!pacijentId || loading.karton}
              onClick={() => preuzmi('karton', `/izvestaji/karton/${pacijentId}`, `karton-${pacijentId}.pdf`)}
            >
              {loading.karton ? 'Preuzimanje...' : 'Preuzmi PDF'}
            </button>
          </div>
        </div>

      </div>
    </div>
  )
}
