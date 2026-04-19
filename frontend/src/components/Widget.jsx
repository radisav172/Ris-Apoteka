import { useEffect, useState } from 'react'
import { Link } from 'react-router-dom'
import { api } from '../api'
import styles from './Widget.module.css'

export default function Widget({ title, endpoint, to, columns }) {
  const [rows, setRows] = useState([])
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    api.get(endpoint)
      .then(data => setRows(data.slice(0, 5)))
      .catch(() => setRows([]))
      .finally(() => setLoading(false))
  }, [endpoint])

  return (
    <div className={styles.card}>
      <div className={styles.header}>
        <span className={styles.title}>{title}</span>
        <Link to={to} className={styles.more}>Pogledaj sve →</Link>
      </div>
      {loading ? (
        <p className={styles.empty}>Ucitavanje...</p>
      ) : rows.length === 0 ? (
        <p className={styles.empty}>Nema podataka</p>
      ) : (
        <table className={styles.table}>
          <thead>
            <tr>
              {columns.map(c => <th key={c.key}>{c.label}</th>)}
            </tr>
          </thead>
          <tbody>
            {rows.map((row, i) => (
              <tr key={i}>
                {columns.map(c => (
                  <td key={c.key}>{c.render ? c.render(row) : row[c.key]}</td>
                ))}
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  )
}
