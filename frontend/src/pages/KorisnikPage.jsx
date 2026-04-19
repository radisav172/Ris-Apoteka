import { useEffect, useState } from 'react'
import { api } from '../api'
import PageTable from '../components/PageTable'
import Modal from '../components/Modal'
import FormField from '../components/FormField'
import styles from './EntityPage.module.css'

const empty = { ime: '', prezime: '', korisnickoIme: '', lozinkaHash: '', rola: 'korisnik' }

const rolaOptions = [
  { value: 'admin', label: 'Admin' },
  { value: 'korisnik', label: 'Korisnik' },
]

export default function KorisnikPage() {
  const [rows, setRows] = useState([])
  const [modal, setModal] = useState(null)
  const [form, setForm] = useState(empty)
  const [error, setError] = useState('')

  useEffect(() => { load() }, [])

  async function load() { setRows(await api.get('/korisnici')) }

  function openCreate() { setForm(empty); setError(''); setModal('create') }
  function openEdit(row) {
    setForm({ ime: row.ime, prezime: row.prezime, korisnickoIme: row.korisnickoIme, lozinkaHash: '', rola: row.rola })
    setError(''); setModal({ type: 'edit', id: row.id })
  }
  function close() { setModal(null) }

  function handleChange(e) { setForm(f => ({ ...f, [e.target.name]: e.target.value })) }

  async function handleSave(e) {
    e.preventDefault(); setError('')
    try {
      modal === 'create' ? await api.post('/korisnici', form) : await api.put(`/korisnici/${modal.id}`, form)
      await load(); close()
    } catch { setError('Greska — korisnicko ime mozda vec postoji') }
  }

  async function handleDelete(row) {
    if (!confirm(`Obrisi korisnika "${row.korisnickoIme}"?`)) return
    try { await api.delete(`/korisnici/${row.id}`); await load() }
    catch { alert('Greska pri brisanju') }
  }

  const columns = [
    { key: 'id', label: 'ID' },
    { key: 'korisnickoIme', label: 'Korisnicko ime' },
    { key: 'ime', label: 'Ime', render: r => `${r.ime} ${r.prezime}` },
    { key: 'rola', label: 'Rola', render: r => (
      <span className={`${styles.badge} ${r.rola === 'admin' ? styles.badgeBlue : styles.badgeGray}`}>
        {r.rola}
      </span>
    )},
  ]

  return (
    <div>
      <div className={styles.top}>
        <h2 className={styles.heading}>Korisnici</h2>
        <button className={styles.btn} onClick={openCreate}>+ Dodaj</button>
      </div>
      <PageTable columns={columns} rows={rows} onEdit={openEdit} onDelete={handleDelete} />

      {modal && (
        <Modal title={modal === 'create' ? 'Novi korisnik' : 'Izmeni korisnika'} onClose={close}>
          <form onSubmit={handleSave}>
            <FormField label="Ime" name="ime" value={form.ime} onChange={handleChange} required />
            <FormField label="Prezime" name="prezime" value={form.prezime} onChange={handleChange} required />
            <FormField label="Korisnicko ime" name="korisnickoIme" value={form.korisnickoIme} onChange={handleChange} required />
            <FormField label={modal === 'create' ? 'Lozinka' : 'Nova lozinka'} name="lozinkaHash" type="password" value={form.lozinkaHash} onChange={handleChange} required={modal === 'create'} />
            <FormField label="Rola" name="rola" value={form.rola} onChange={handleChange} options={rolaOptions} required />
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
