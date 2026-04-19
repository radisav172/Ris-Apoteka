import { useEffect, useState } from 'react'
import { api } from '../api'
import PageTable from '../components/PageTable'
import Modal from '../components/Modal'
import FormField from '../components/FormField'
import styles from './EntityPage.module.css'

const empty = { ime: '', prezime: '', jmbg: '', datumRodjenja: '', kontakt: '' }

export default function PacijentPage() {
  const [rows, setRows] = useState([])
  const [modal, setModal] = useState(null)
  const [form, setForm] = useState(empty)
  const [error, setError] = useState('')
  const [detail, setDetail] = useState(null)
  const [alergije, setAlergije] = useState([])
  const [istorija, setIstorija] = useState([])
  const [lekovi, setLekovi] = useState([])
  const [noviLekId, setNoviLekId] = useState('')

  useEffect(() => { load(); api.get('/lekovi').then(setLekovi) }, [])

  async function load() { setRows(await api.get('/pacijenti')) }

  function openCreate() { setForm(empty); setError(''); setModal('create') }
  function openEdit(row) {
    setForm({ ime: row.ime, prezime: row.prezime, jmbg: row.jmbg, datumRodjenja: row.datumRodjenja ?? '', kontakt: row.kontakt ?? '' })
    setError(''); setModal({ type: 'edit', id: row.id })
  }
  async function openDetail(row) {
    const [al, ist] = await Promise.all([api.get(`/pacijenti/${row.id}/alergije`), api.get(`/pacijenti/${row.id}/istorija`)])
    setAlergije(al); setIstorija(ist); setDetail(row)
  }
  function close() { setModal(null); setDetail(null) }

  function handleChange(e) { setForm(f => ({ ...f, [e.target.name]: e.target.value })) }

  async function handleSave(e) {
    e.preventDefault(); setError('')
    try {
      modal === 'create' ? await api.post('/pacijenti', form) : await api.put(`/pacijenti/${modal.id}`, form)
      await load(); close()
    } catch { setError('Greska pri cuvanju') }
  }

  async function handleDelete(row) {
    if (!confirm(`Obrisi pacijenta "${row.ime} ${row.prezime}"?`)) return
    try { await api.delete(`/pacijenti/${row.id}`); await load() }
    catch { alert('Greska pri brisanju') }
  }

  async function addAlergija() {
    if (!noviLekId) return
    await api.post(`/pacijenti/${detail.id}/alergije/${noviLekId}`)
    setAlergije(await api.get(`/pacijenti/${detail.id}/alergije`))
    setNoviLekId('')
  }

  async function removeAlergija(lekId) {
    await api.delete(`/pacijenti/${detail.id}/alergije/${lekId}`)
    setAlergije(await api.get(`/pacijenti/${detail.id}/alergije`))
  }

  const columns = [
    { key: 'id', label: 'ID' },
    { key: 'ime', label: 'Ime i prezime', render: r => `${r.ime} ${r.prezime}` },
    { key: 'jmbg', label: 'JMBG' },
    { key: 'kontakt', label: 'Kontakt' },
    { key: 'detalji', label: '', render: r => (
      <button className={styles.btnSecondary} style={{ padding: '4px 10px', fontSize: 12 }} onClick={() => openDetail(r)}>Detalji</button>
    )},
  ]

  return (
    <div>
      <div className={styles.top}>
        <h2 className={styles.heading}>Pacijenti</h2>
        <button className={styles.btn} onClick={openCreate}>+ Dodaj</button>
      </div>
      <PageTable columns={columns} rows={rows} onEdit={openEdit} onDelete={handleDelete} />

      {modal && (
        <Modal title={modal === 'create' ? 'Novi pacijent' : 'Izmeni pacijenta'} onClose={close}>
          <form onSubmit={handleSave}>
            <FormField label="Ime" name="ime" value={form.ime} onChange={handleChange} required />
            <FormField label="Prezime" name="prezime" value={form.prezime} onChange={handleChange} required />
            <FormField label="JMBG" name="jmbg" value={form.jmbg} onChange={handleChange} required />
            <FormField label="Datum rodjenja" name="datumRodjenja" type="date" value={form.datumRodjenja} onChange={handleChange} />
            <FormField label="Kontakt" name="kontakt" value={form.kontakt} onChange={handleChange} />
            {error && <p className={styles.error}>{error}</p>}
            <div className={styles.formActions}>
              <button type="button" className={styles.btnSecondary} onClick={close}>Otkazi</button>
              <button type="submit" className={styles.btn}>Sacuvaj</button>
            </div>
          </form>
        </Modal>
      )}

      {detail && (
        <Modal title={`${detail.ime} ${detail.prezime}`} onClose={close}>
          <p style={{ fontSize: 13, color: 'var(--text-muted)', marginBottom: 16 }}>JMBG: {detail.jmbg} | Kontakt: {detail.kontakt ?? '-'}</p>

          <h4 style={{ margin: '0 0 10px', fontSize: 14 }}>Alergije</h4>
          {alergije.length === 0 ? <p style={{ fontSize: 13, color: 'var(--text-muted)' }}>Nema alergija</p> : (
            <ul style={{ margin: '0 0 10px', paddingLeft: 20 }}>
              {alergije.map(l => (
                <li key={l.id} style={{ fontSize: 13, marginBottom: 4 }}>
                  {l.naziv}
                  <button onClick={() => removeAlergija(l.id)} style={{ marginLeft: 8, fontSize: 11, color: 'var(--error)', background: 'none', border: 'none', cursor: 'pointer' }}>ukloni</button>
                </li>
              ))}
            </ul>
          )}
          <div style={{ display: 'flex', gap: 8, marginBottom: 20 }}>
            <select value={noviLekId} onChange={e => setNoviLekId(e.target.value)} style={{ flex: 1, padding: '7px 10px', borderRadius: 8, border: '1px solid var(--border)', fontSize: 13 }}>
              <option value="">Dodaj alergiju...</option>
              {lekovi.map(l => <option key={l.id} value={l.id}>{l.naziv}</option>)}
            </select>
            <button className={styles.btn} style={{ padding: '7px 14px' }} onClick={addAlergija}>Dodaj</button>
          </div>

          <h4 style={{ margin: '0 0 10px', fontSize: 14 }}>Istorija kupovina</h4>
          {istorija.length === 0 ? <p style={{ fontSize: 13, color: 'var(--text-muted)' }}>Nema kupovina</p> : (
            <table style={{ width: '100%', fontSize: 13, borderCollapse: 'collapse' }}>
              <thead>
                <tr style={{ background: 'var(--bg)' }}>
                  <th style={{ padding: '6px 8px', textAlign: 'left' }}>Datum</th>
                  <th style={{ padding: '6px 8px', textAlign: 'left' }}>Napomena</th>
                </tr>
              </thead>
              <tbody>
                {istorija.map(p => (
                  <tr key={p.id}>
                    <td style={{ padding: '6px 8px' }}>{p.datum?.slice(0, 10)}</td>
                    <td style={{ padding: '6px 8px' }}>{p.napomena ?? '-'}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </Modal>
      )}
    </div>
  )
}
