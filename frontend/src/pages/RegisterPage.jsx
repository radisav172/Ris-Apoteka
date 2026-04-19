import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { api } from '../api'
import { saveAuth } from '../auth'
import styles from './AuthPage.module.css'

export default function RegisterPage() {
  const navigate = useNavigate()
  const [form, setForm] = useState({ ime: '', prezime: '', korisnickoIme: '', lozinka: '', rola: 'korisnik' })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  function handleChange(e) {
    setForm(f => ({ ...f, [e.target.name]: e.target.value }))
  }

  async function handleSubmit(e) {
    e.preventDefault()
    setError('')
    setLoading(true)
    try {
      const data = await api.post('/auth/register', form)
      saveAuth(data.token, data.rola)
      navigate('/')
    } catch (err) {
      setError('Korisnicko ime je zauzeto ili su podaci neispravni')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className={styles.page}>
      <div className={styles.card}>
        <h1 className={styles.title}>Apoteka</h1>
        <h2 className={styles.subtitle}>Registracija</h2>

        <form onSubmit={handleSubmit} className={styles.form}>
          <div className={styles.row}>
            <div className={styles.field}>
              <label htmlFor="ime">Ime</label>
              <input
                id="ime"
                name="ime"
                type="text"
                value={form.ime}
                onChange={handleChange}
                required
              />
            </div>
            <div className={styles.field}>
              <label htmlFor="prezime">Prezime</label>
              <input
                id="prezime"
                name="prezime"
                type="text"
                value={form.prezime}
                onChange={handleChange}
                required
              />
            </div>
          </div>

          <div className={styles.field}>
            <label htmlFor="korisnickoIme">Korisnicko ime</label>
            <input
              id="korisnickoIme"
              name="korisnickoIme"
              type="text"
              autoComplete="username"
              value={form.korisnickoIme}
              onChange={handleChange}
              required
            />
          </div>

          <div className={styles.field}>
            <label htmlFor="lozinka">Lozinka</label>
            <input
              id="lozinka"
              name="lozinka"
              type="password"
              autoComplete="new-password"
              value={form.lozinka}
              onChange={handleChange}
              required
            />
          </div>

          {error && <p className={styles.error}>{error}</p>}

          <button type="submit" className={styles.btn} disabled={loading}>
            {loading ? 'Registracija...' : 'Registruj se'}
          </button>
        </form>

        <p className={styles.link}>
          Vec imate nalog? <Link to="/login">Prijavite se</Link>
        </p>
      </div>
    </div>
  )
}
