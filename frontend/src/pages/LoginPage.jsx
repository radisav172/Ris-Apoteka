import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { api } from '../api'
import { saveAuth } from '../auth'
import styles from './AuthPage.module.css'

export default function LoginPage() {
  const navigate = useNavigate()
  const [form, setForm] = useState({ korisnickoIme: '', lozinka: '' })
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
      const data = await api.post('/auth/login', form)
      saveAuth(data.token, data.rola)
      navigate('/')
    } catch (err) {
      setError('Pogresno korisnicko ime ili lozinka')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className={styles.page}>
      <div className={styles.card}>
        <h1 className={styles.title}>Apoteka</h1>
        <h2 className={styles.subtitle}>Prijava</h2>

        <form onSubmit={handleSubmit} className={styles.form}>
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
              autoComplete="current-password"
              value={form.lozinka}
              onChange={handleChange}
              required
            />
          </div>

          {error && <p className={styles.error}>{error}</p>}

          <button type="submit" className={styles.btn} disabled={loading}>
            {loading ? 'Prijava...' : 'Prijavi se'}
          </button>
        </form>

        <p className={styles.link}>
          Nemate nalog? <Link to="/register">Registrujte se</Link>
        </p>
      </div>
    </div>
  )
}
