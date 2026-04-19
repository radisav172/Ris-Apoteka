import { useEffect, useState } from 'react'
import { api } from '../api'
import PageTable from '../components/PageTable'
import Modal from '../components/Modal'
import FormField from '../components/FormField'
import { useRole } from '../useRole'
import styles from './EntityPage.module.css'

export default function ProdajaPage() {
  const { isAdmin } = useRole()
  const [rows, setRows] = useState([])
  const [pacijenti, setPacijenti] = useState([])
  const [korisnici, setKorisnici] = useState([])
  const [recepti, setRecepti] = useState([])
  const [lekovi, setLekovi] = useState([])
  const [modal, setModal] = useState(null)
  const [form, setForm] = useState({ napomena: '', korisnikId: '', pacijentId: '', receptId: '' })
  const [stavke, setStavke] = useState([{ lekId: '', kolicina: 1, cenaProdaje: '' }])
  const [error, setError] = useState('')
  const [periodOd, setPeriodOd] = useState('')
  const [periodDo, setPeriodDo] = useState('')

  useEffect(() => {
    load()
    api.get('/pacijenti').then(setPacijenti)
    api.get('/korisnici').then(setKorisnici)
    api.get('/recepti').then(r => setRecepti(r.filter(x => x.status === 'aktivan')))
    api.get('/lekovi').then(setLekovi)
  }, [])

  async function load() { setRows(await api.get('/prodaje')) }

  async function filterPeriod() {
    if (!periodOd || !periodDo) return
    setRows(await api.get(`/prodaje/period?od=${periodOd}&do_=${periodDo}`))
  }

  function openCreate() {
    setForm({ napomena: '', korisnikId: '', pacijentId: '', receptId: '' })
    setStavke([{ lekId: '', kolicina: 1, cenaProdaje: '' }])
    setError(''); setModal('create')
  }
  function close() { setModal(null) }

  function handleChange(e) { setForm(f => ({ ...f, [e.target.name]: e.target.value })) }

  function stavkaChange(i, e) {
    setStavke(s => s.map((st, idx) => idx === i ? { ...st, [e.target.name]: e.target.value } : st))
  }

  function addStavka() { setStavke(s => [...s, { lekId: '', kolicina: 1, cenaProdaje: '' }]) }
  function removeStavka(i) { setStavke(s => s.filter((_, idx) => idx !== i)) }

  async function handleSave(e) {
    e.preventDefault(); setError('')
    try {
      await api.post('/prodaje', { ...form, stavke })
      await load(); close()
    } catch { setError('Greska pri cuvanju') }
  }

  async function handleDelete(row) {
    if (!confirm('Obrisi prodaju?')) return
    try { await api.delete(`/prodaje/${row.id}`); await load() }
    catch { alert('Greska pri brisanju') }
  }

  const columns = [
    { key: 'id', label: 'ID' },
    { key: 'datum', label: 'Datum', render: r => r.datum?.slice(0, 10) },
    { key: 'korisnik', label: 'Korisnik', render: r => r.korisnik ? `${r.korisnik.ime} ${r.korisnik.prezime}` : '-' },
    { key: 'pacijent', label: 'Pacijent', render: r => r.pacijent ? `${r.pacijent.ime} ${r.pacijent.prezime}` : '-' },
    { key: 'napomena', label: 'Napomena', render: r => r.napomena ?? '-' },
  ]

  const pacijentOptions = pacijenti.map(p => ({ value: p.id, label: `${p.ime} ${p.prezime}` }))
  const korisnikOptions = korisnici.map(k => ({ value: k.id, label: k.korisnickoIme }))
  const receptOptions = recepti.map(r => ({ value: r.id, label: `${r.brojRecepta} (${r.pacijent?.ime ?? '-'})` }))
  const lekOptions = lekovi.map(l => ({ value: l.id, label: l.naziv }))

  return (
    <div>
      <div className={styles.top}>
        <h2 className={styles.heading}>Prodaje</h2>
        <button className={styles.btn} onClick={openCreate}>+ Nova prodaja</button>
      </div>

      <div style={{ display: 'flex', gap: 10, marginBottom: 16, alignItems: 'flex-end' }}>
        <div>
          <label style={{ fontSize: 12, color: 'var(--text-muted)', display: 'block', marginBottom: 4 }}>Od</label>
          <input type="date" value={periodOd} onChange={e => setPeriodOd(e.target.value)} style={{ padding: '8px 10px', borderRadius: 8, border: '1px solid var(--border)', fontSize: 14 }} />
        </div>
        <div>
          <label style={{ fontSize: 12, color: 'var(--text-muted)', display: 'block', marginBottom: 4 }}>Do</label>
          <input type="date" value={periodDo} onChange={e => setPeriodDo(e.target.value)} style={{ padding: '8px 10px', borderRadius: 8, border: '1px solid var(--border)', fontSize: 14 }} />
        </div>
        <button className={styles.btnSecondary} style={{ padding: '9px 16px' }} onClick={filterPeriod}>Filtriraj</button>
        <button className={styles.btnSecondary} style={{ padding: '9px 16px' }} onClick={load}>Resetuj</button>
      </div>

      <PageTable columns={columns} rows={rows} onDelete={isAdmin ? handleDelete : null} />

      {modal && (
        <Modal title="Nova prodaja" onClose={close}>
          <form onSubmit={handleSave}>
            <FormField label="Korisnik" name="korisnikId" value={form.korisnikId} onChange={handleChange} options={korisnikOptions} required />
            <FormField label="Pacijent" name="pacijentId" value={form.pacijentId} onChange={handleChange} options={pacijentOptions} />
            <FormField label="Recept" name="receptId" value={form.receptId} onChange={handleChange} options={receptOptions} />
            <FormField label="Napomena" name="napomena" value={form.napomena} onChange={handleChange} />

            <div style={{ margin: '16px 0 8px', fontWeight: 600, fontSize: 13 }}>Stavke</div>
            {stavke.map((st, i) => (
              <div key={i} style={{ display: 'grid', gridTemplateColumns: '1fr 70px 100px 28px', gap: 6, marginBottom: 8, alignItems: 'end' }}>
                <select name="lekId" value={st.lekId} onChange={e => stavkaChange(i, e)} required style={{ padding: '8px 10px', borderRadius: 8, border: '1px solid var(--border)', fontSize: 13 }}>
                  <option value="">Lek...</option>
                  {lekOptions.map(o => <option key={o.value} value={o.value}>{o.label}</option>)}
                </select>
                <input name="kolicina" type="number" min="1" value={st.kolicina} onChange={e => stavkaChange(i, e)} placeholder="Kol." required style={{ padding: '8px 8px', borderRadius: 8, border: '1px solid var(--border)', fontSize: 13 }} />
                <input name="cenaProdaje" type="number" step="0.01" value={st.cenaProdaje} onChange={e => stavkaChange(i, e)} placeholder="Cena" required style={{ padding: '8px 8px', borderRadius: 8, border: '1px solid var(--border)', fontSize: 13 }} />
                {stavke.length > 1 && <button type="button" onClick={() => removeStavka(i)} style={{ background: 'none', border: 'none', color: 'var(--error)', cursor: 'pointer', fontSize: 16, padding: 0 }}>✕</button>}
              </div>
            ))}
            <button type="button" className={styles.btnSecondary} style={{ fontSize: 12, padding: '6px 12px', marginBottom: 12 }} onClick={addStavka}>+ Stavka</button>

            {error && <p className={styles.error}>{error}</p>}
            <div className={styles.formActions}>
              <button type="button" className={styles.btnSecondary} onClick={close}>Otkazi</button>
              <button type="submit" className={styles.btn}>Sacuvaj</button>
            </div>
          </form>
        </Modal>
      )}
    </div>
  )
}
