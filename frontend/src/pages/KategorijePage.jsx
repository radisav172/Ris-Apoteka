import { useEffect, useState } from 'react'
import { api } from '../api'
import PageTable from '../components/PageTable'
import Modal from '../components/Modal'
import FormField from '../components/FormField'
import { useRole } from '../useRole'
import styles from './EntityPage.module.css'

const empty = { naziv: '', opis: '' }

export default function KategorijePage() {
  const { isAdmin } = useRole()
  const [rows, setRows] = useState([])
  const [modal, setModal] = useState(null)
  const [form, setForm] = useState(empty)
  const [error, setError] = useState('')

  useEffect(() => { load() }, [])

  async function load() {
    setRows(await api.get('/kategorije'))
  }

  function openCreate() { setForm(empty); setError(''); setModal('create') }
  function openEdit(row) { setForm({ naziv: row.naziv, opis: row.opis ?? '' }); setError(''); setModal({ type: 'edit', id: row.id }) }
  function close() { setModal(null) }

  function handleChange(e) {
    setForm(f => ({ ...f, [e.target.name]: e.target.value }))
  }

  async function handleSave(e) {
    e.preventDefault()
    setError('')
    try {
      if (modal === 'create') {
        await api.post('/kategorije', form)
      } else {
        await api.put(`/kategorije/${modal.id}`, form)
      }
      await load()
      close()
    } catch {
      setError('Greska pri cuvanju')
    }
  }

  async function handleDelete(row) {
    if (!confirm(`Obrisi kategoriju "${row.naziv}"?`)) return
    try {
      await api.delete(`/kategorije/${row.id}`)
      await load()
    } catch {
      alert('Greska pri brisanju')
    }
  }

  const columns = [
    { key: 'id', label: 'ID' },
    { key: 'naziv', label: 'Naziv' },
    { key: 'opis', label: 'Opis' },
  ]

  return (
    <div>
      <div className={styles.top}>
        <h2 className={styles.heading}>Kategorije lekova</h2>
        {isAdmin && <button className={styles.btn} onClick={openCreate}>+ Dodaj</button>}
      </div>
      <PageTable columns={columns} rows={rows} onEdit={isAdmin ? openEdit : null} onDelete={isAdmin ? handleDelete : null} />

      {modal && (
        <Modal title={modal === 'create' ? 'Nova kategorija' : 'Izmeni kategoriju'} onClose={close}>
          <form onSubmit={handleSave}>
            <FormField label="Naziv" name="naziv" value={form.naziv} onChange={handleChange} required />
            <FormField label="Opis" name="opis" value={form.opis} onChange={handleChange} />
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
