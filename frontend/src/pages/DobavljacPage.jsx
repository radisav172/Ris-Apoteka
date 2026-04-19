import { useEffect, useState } from 'react'
import { api } from '../api'
import PageTable from '../components/PageTable'
import Modal from '../components/Modal'
import FormField from '../components/FormField'
import { useRole } from '../useRole'
import styles from './EntityPage.module.css'

const empty = { naziv: '', adresa: '', kontakt: '', pib: '' }

export default function DobavljacPage() {
  const { isAdmin } = useRole()
  const [rows, setRows] = useState([])
  const [modal, setModal] = useState(null)
  const [form, setForm] = useState(empty)
  const [error, setError] = useState('')

  useEffect(() => { load() }, [])

  async function load() { setRows(await api.get('/dobavljaci')) }

  function openCreate() { setForm(empty); setError(''); setModal('create') }
  function openEdit(row) { setForm({ naziv: row.naziv, adresa: row.adresa ?? '', kontakt: row.kontakt ?? '', pib: row.pib ?? '' }); setError(''); setModal({ type: 'edit', id: row.id }) }
  function close() { setModal(null) }

  function handleChange(e) { setForm(f => ({ ...f, [e.target.name]: e.target.value })) }

  async function handleSave(e) {
    e.preventDefault(); setError('')
    try {
      modal === 'create' ? await api.post('/dobavljaci', form) : await api.put(`/dobavljaci/${modal.id}`, form)
      await load(); close()
    } catch { setError('Greska pri cuvanju') }
  }

  async function handleDelete(row) {
    if (!confirm(`Obrisi dobavljaca "${row.naziv}"?`)) return
    try { await api.delete(`/dobavljaci/${row.id}`); await load() }
    catch { alert('Greska pri brisanju — dobavljac mozda ima nabavke') }
  }

  const columns = [
    { key: 'id', label: 'ID' },
    { key: 'naziv', label: 'Naziv' },
    { key: 'adresa', label: 'Adresa' },
    { key: 'kontakt', label: 'Kontakt' },
    { key: 'pib', label: 'PIB' },
  ]

  return (
    <div>
      <div className={styles.top}>
        <h2 className={styles.heading}>Dobavljaci</h2>
        {isAdmin && <button className={styles.btn} onClick={openCreate}>+ Dodaj</button>}
      </div>
      <PageTable columns={columns} rows={rows} onEdit={isAdmin ? openEdit : null} onDelete={isAdmin ? handleDelete : null} />

      {modal && (
        <Modal title={modal === 'create' ? 'Novi dobavljac' : 'Izmeni dobavljaca'} onClose={close}>
          <form onSubmit={handleSave}>
            <FormField label="Naziv" name="naziv" value={form.naziv} onChange={handleChange} required />
            <FormField label="Adresa" name="adresa" value={form.adresa} onChange={handleChange} />
            <FormField label="Kontakt" name="kontakt" value={form.kontakt} onChange={handleChange} />
            <FormField label="PIB" name="pib" value={form.pib} onChange={handleChange} />
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
