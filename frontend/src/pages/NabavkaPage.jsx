import { useEffect, useState } from 'react'
import { api } from '../api'
import PageTable from '../components/PageTable'
import Modal from '../components/Modal'
import FormField from '../components/FormField'
import { useRole } from '../useRole'
import styles from './EntityPage.module.css'

export default function NabavkaPage() {
  const { isAdmin } = useRole()
  const [rows, setRows] = useState([])
  const [dobavljaci, setDobavljaci] = useState([])
  const [lekovi, setLekovi] = useState([])
  const [modal, setModal] = useState(null)
  const [form, setForm] = useState({ napomena: '', dobavljacId: '' })
  const [stavke, setStavke] = useState([{ lekId: '', kolicina: 1, nabavnaCena: '' }])
  const [error, setError] = useState('')

  useEffect(() => {
    load()
    api.get('/dobavljaci').then(setDobavljaci)
    api.get('/lekovi').then(setLekovi)
  }, [])

  async function load() { setRows(await api.get('/nabavke')) }

  function openCreate() {
    setForm({ napomena: '', dobavljacId: '' })
    setStavke([{ lekId: '', kolicina: 1, nabavnaCena: '' }])
    setError(''); setModal('create')
  }
  function close() { setModal(null) }

  function handleChange(e) { setForm(f => ({ ...f, [e.target.name]: e.target.value })) }

  function stavkaChange(i, e) {
    setStavke(s => s.map((st, idx) => idx === i ? { ...st, [e.target.name]: e.target.value } : st))
  }

  function addStavka() { setStavke(s => [...s, { lekId: '', kolicina: 1, nabavnaCena: '' }]) }
  function removeStavka(i) { setStavke(s => s.filter((_, idx) => idx !== i)) }

  async function handleSave(e) {
    e.preventDefault(); setError('')
    try {
      await api.post('/nabavke', { ...form, stavke })
      await load(); close()
    } catch { setError('Greska pri cuvanju') }
  }

  async function handleDelete(row) {
    if (!confirm('Obrisi nabavku?')) return
    try { await api.delete(`/nabavke/${row.id}`); await load() }
    catch { alert('Greska pri brisanju') }
  }

  const columns = [
    { key: 'id', label: 'ID' },
    { key: 'datum', label: 'Datum', render: r => r.datum?.slice(0, 10) },
    { key: 'dobavljac', label: 'Dobavljac', render: r => r.dobavljac?.naziv ?? '-' },
    { key: 'napomena', label: 'Napomena', render: r => r.napomena ?? '-' },
  ]

  const dobavljacOptions = dobavljaci.map(d => ({ value: d.id, label: d.naziv }))
  const lekOptions = lekovi.map(l => ({ value: l.id, label: l.naziv }))

  return (
    <div>
      <div className={styles.top}>
        <h2 className={styles.heading}>Nabavke</h2>
        <button className={styles.btn} onClick={openCreate}>+ Nova nabavka</button>
      </div>
      <PageTable columns={columns} rows={rows} onDelete={isAdmin ? handleDelete : null} />

      {modal && (
        <Modal title="Nova nabavka" onClose={close}>
          <form onSubmit={handleSave}>
            <FormField label="Dobavljac" name="dobavljacId" value={form.dobavljacId} onChange={handleChange} options={dobavljacOptions} required />
            <FormField label="Napomena" name="napomena" value={form.napomena} onChange={handleChange} />

            <div style={{ margin: '16px 0 8px', fontWeight: 600, fontSize: 13 }}>Stavke</div>
            {stavke.map((st, i) => (
              <div key={i} style={{ display: 'grid', gridTemplateColumns: '1fr 70px 100px 28px', gap: 6, marginBottom: 8, alignItems: 'end' }}>
                <select name="lekId" value={st.lekId} onChange={e => stavkaChange(i, e)} required style={{ padding: '8px 10px', borderRadius: 8, border: '1px solid var(--border)', fontSize: 13 }}>
                  <option value="">Lek...</option>
                  {lekOptions.map(o => <option key={o.value} value={o.value}>{o.label}</option>)}
                </select>
                <input name="kolicina" type="number" min="1" value={st.kolicina} onChange={e => stavkaChange(i, e)} placeholder="Kol." required style={{ padding: '8px 8px', borderRadius: 8, border: '1px solid var(--border)', fontSize: 13 }} />
                <input name="nabavnaCena" type="number" step="0.01" value={st.nabavnaCena} onChange={e => stavkaChange(i, e)} placeholder="Cena" required style={{ padding: '8px 8px', borderRadius: 8, border: '1px solid var(--border)', fontSize: 13 }} />
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
