import { useEffect, useState } from 'react'
import { api } from '../api'
import PageTable from '../components/PageTable'
import Modal from '../components/Modal'
import FormField from '../components/FormField'
import styles from './EntityPage.module.css'

const empty = { brojRecepta: '', datum: '', pacijentId: '', korisnikId: '' }

export default function ReceptPage() {
  const [rows, setRows] = useState([])
  const [pacijenti, setPacijenti] = useState([])
  const [korisnici, setKorisnici] = useState([])
  const [modal, setModal] = useState(null)
  const [form, setForm] = useState(empty)
  const [error, setError] = useState('')

  useEffect(() => {
    load()
    api.get('/pacijenti').then(setPacijenti)
    api.get('/korisnici').then(setKorisnici)
  }, [])

  async function load() { setRows(await api.get('/recepti')) }

  function openCreate() { setForm(empty); setError(''); setModal('create') }
  function close() { setModal(null) }

  function handleChange(e) { setForm(f => ({ ...f, [e.target.name]: e.target.value })) }

  async function handleSave(e) {
    e.preventDefault(); setError('')
    try {
      await api.post('/recepti', form)
      await load(); close()
    } catch { setError('Greska pri cuvanju') }
  }

  async function handleIskoristi(row) {
    if (row.status !== 'aktivan') return alert('Recept nije aktivan')
    if (!confirm(`Iskoristi recept "${row.brojRecepta}"?`)) return
    try { await api.put(`/recepti/${row.id}/iskoristi`); await load() }
    catch { alert('Greska pri koriscenju recepta') }
  }

  const statusBadge = (status) => {
    const map = { aktivan: styles.badgeGreen, iskoriscen: styles.badgeGray, istekao: styles.badgeRed }
    return <span className={`${styles.badge} ${map[status] ?? styles.badgeGray}`}>{status}</span>
  }

  const columns = [
    { key: 'id', label: 'ID' },
    { key: 'brojRecepta', label: 'Broj recepta' },
    { key: 'datum', label: 'Datum', render: r => r.datum?.slice(0, 10) },
    { key: 'status', label: 'Status', render: r => statusBadge(r.status) },
    { key: 'pacijent', label: 'Pacijent', render: r => r.pacijent ? `${r.pacijent.ime} ${r.pacijent.prezime}` : '-' },
    { key: 'iskoristi', label: '', render: r => r.status === 'aktivan' ? (
      <button className={styles.btn} style={{ padding: '4px 12px', fontSize: 12 }} onClick={() => handleIskoristi(r)}>Iskoristi</button>
    ) : null },
  ]

  const pacijentOptions = pacijenti.map(p => ({ value: p.id, label: `${p.ime} ${p.prezime}` }))
  const korisnikOptions = korisnici.map(k => ({ value: k.id, label: k.korisnickoIme }))

  return (
    <div>
      <div className={styles.top}>
        <h2 className={styles.heading}>Recepti</h2>
        <button className={styles.btn} onClick={openCreate}>+ Dodaj</button>
      </div>
      <PageTable columns={columns} rows={rows} />

      {modal && (
        <Modal title="Novi recept" onClose={close}>
          <form onSubmit={handleSave}>
            <FormField label="Broj recepta" name="brojRecepta" value={form.brojRecepta} onChange={handleChange} required />
            <FormField label="Datum" name="datum" type="date" value={form.datum} onChange={handleChange} required />
            <FormField label="Pacijent" name="pacijentId" value={form.pacijentId} onChange={handleChange} options={pacijentOptions} required />
            <FormField label="Korisnik" name="korisnikId" value={form.korisnikId} onChange={handleChange} options={korisnikOptions} required />
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
