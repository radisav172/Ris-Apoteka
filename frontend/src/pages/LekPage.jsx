import { useEffect, useState } from 'react'
import { api } from '../api'
import PageTable from '../components/PageTable'
import Modal from '../components/Modal'
import FormField from '../components/FormField'
import { useRole } from '../useRole'
import styles from './EntityPage.module.css'

const empty = { naziv: '', sifra: '', proizvodjac: '', oblik: '', jacinaDoze: '', kolicinaNaStanju: '', minimalnaZaliha: '', rokTrajanja: '', cena: '', naRecept: 'false', kategorijaId: '' }

export default function LekPage() {
  const { isAdmin } = useRole()
  const [rows, setRows] = useState([])
  const [kategorije, setKategorije] = useState([])
  const [modal, setModal] = useState(null)
  const [form, setForm] = useState(empty)
  const [error, setError] = useState('')
  const [filter, setFilter] = useState('sve')
  const [danaDo, setDanaDo] = useState(30)

  useEffect(() => {
    load()
    api.get('/kategorije').then(setKategorije)
  }, [])

  async function load() {
    if (filter === 'niske') {
      setRows(await api.get('/lekovi/niske-zalihe'))
    } else if (filter === 'isticu') {
      setRows(await api.get(`/lekovi/isticu-rok?danaDo=${danaDo}`))
    } else {
      setRows(await api.get('/lekovi'))
    }
  }

  function openCreate() { setForm(empty); setError(''); setModal('create') }
  function openEdit(row) {
    setForm({
      naziv: row.naziv, sifra: row.sifra, proizvodjac: row.proizvodjac,
      oblik: row.oblik ?? '', jacinaDoze: row.jacinaDoze ?? '',
      kolicinaNaStanju: row.kolicinaNaStanju, minimalnaZaliha: row.minimalnaZaliha,
      rokTrajanja: row.rokTrajanja ?? '', cena: row.cena,
      naRecept: String(row.naRecept), kategorijaId: row.kategorija?.id ?? '',
    })
    setError(''); setModal({ type: 'edit', id: row.id })
  }
  function close() { setModal(null) }

  function handleChange(e) { setForm(f => ({ ...f, [e.target.name]: e.target.value })) }

  async function handleSave(e) {
    e.preventDefault(); setError('')
    const payload = { ...form, naRecept: form.naRecept === 'true', kategorijaId: form.kategorijaId || null }
    try {
      modal === 'create' ? await api.post('/lekovi', payload) : await api.put(`/lekovi/${modal.id}`, payload)
      await load(); close()
    } catch { setError('Greska pri cuvanju') }
  }

  async function handleDelete(row) {
    if (!confirm(`Obrisi lek "${row.naziv}"?`)) return
    try { await api.delete(`/lekovi/${row.id}`); await load() }
    catch { alert('Greska pri brisanju — lek mozda ima stavke') }
  }

  useEffect(() => { load() }, [filter])

  const visibleRows = rows

  const columns = [
    { key: 'naziv', label: 'Naziv' },
    { key: 'sifra', label: 'Sifra' },
    { key: 'kategorija', label: 'Kategorija', render: r => r.kategorija?.naziv ?? '-' },
    { key: 'kolicinaNaStanju', label: 'Zaliha', render: r => {
      const low = r.kolicinaNaStanju <= r.minimalnaZaliha
      return <span className={`${styles.badge} ${low ? styles.badgeRed : styles.badgeGreen}`}>{r.kolicinaNaStanju}</span>
    }},
    { key: 'cena', label: 'Cena (RSD)' },
    { key: 'naRecept', label: 'Na recept', render: r => r.naRecept ? 'Da' : 'Ne' },
  ]

  const katOptions = kategorije.map(k => ({ value: k.id, label: k.naziv }))
  const receptOptions = [{ value: 'true', label: 'Da' }, { value: 'false', label: 'Ne' }]

  return (
    <div>
      <div className={styles.top}>
        <h2 className={styles.heading}>Lekovi</h2>
        <div style={{ display: 'flex', gap: 10, alignItems: 'center' }}>
          <select value={filter} onChange={e => setFilter(e.target.value)} style={{ padding: '8px 12px', borderRadius: 8, border: '1px solid var(--border)', fontSize: 14 }}>
            <option value="sve">Svi lekovi</option>
            <option value="niske">Niske zalihe</option>
            <option value="isticu">Istice rok</option>
          </select>
          {filter === 'isticu' && (

            <div style={{ display: 'flex', alignItems: 'center', gap: 6, fontSize: 13, color: 'var(--text-muted)' }}>
              u narednih
              <input
                type="number"
                min="1"
                value={danaDo}
                onChange={e => setDanaDo(e.target.value)}
                style={{ width: 60, padding: '7px 8px', borderRadius: 8, border: '1px solid var(--border)', fontSize: 13 }}
              />
              dana
              <button className={styles.btnSecondary} style={{ padding: '7px 12px' }} onClick={load}>Primeni</button>
            </div>
          )}
          {isAdmin && <button className={styles.btn} onClick={openCreate}>+ Dodaj</button>}
        </div>
      </div>
      <PageTable columns={columns} rows={visibleRows} onEdit={isAdmin ? openEdit : null} onDelete={isAdmin ? handleDelete : null} />

      {modal && (
        <Modal title={modal === 'create' ? 'Novi lek' : 'Izmeni lek'} onClose={close}>
          <form onSubmit={handleSave}>
            <FormField label="Naziv" name="naziv" value={form.naziv} onChange={handleChange} required />
            <FormField label="Sifra" name="sifra" value={form.sifra} onChange={handleChange} required />
            <FormField label="Proizvodjac" name="proizvodjac" value={form.proizvodjac} onChange={handleChange} required />
            <FormField label="Oblik" name="oblik" value={form.oblik} onChange={handleChange} />
            <FormField label="Jacina doze" name="jacinaDoze" value={form.jacinaDoze} onChange={handleChange} />
            <FormField label="Kolicina na stanju" name="kolicinaNaStanju" type="number" value={form.kolicinaNaStanju} onChange={handleChange} required />
            <FormField label="Minimalna zaliha" name="minimalnaZaliha" type="number" value={form.minimalnaZaliha} onChange={handleChange} required />
            <FormField label="Rok trajanja" name="rokTrajanja" type="date" value={form.rokTrajanja} onChange={handleChange} />
            <FormField label="Cena (RSD)" name="cena" type="number" value={form.cena} onChange={handleChange} required />
            <FormField label="Na recept" name="naRecept" value={form.naRecept} onChange={handleChange} options={receptOptions} required />
            <FormField label="Kategorija" name="kategorijaId" value={form.kategorijaId} onChange={handleChange} options={katOptions} />
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
