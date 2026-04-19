import { NavLink, useNavigate } from 'react-router-dom'
import { clearAuth, getRola } from '../auth'
import styles from './Layout.module.css'

const allLinks = [
  { to: '/', label: 'Dashboard', exact: true },
  { to: '/lekovi', label: 'Lekovi' },
  { to: '/pacijenti', label: 'Pacijenti' },
  { to: '/prodaje', label: 'Prodaje' },
  { to: '/nabavke', label: 'Nabavke' },
  { to: '/recepti', label: 'Recepti' },
  { to: '/dobavljaci', label: 'Dobavljaci' },
  { to: '/kategorije', label: 'Kategorije' },
  { to: '/korisnici', label: 'Korisnici', adminOnly: true },
  { to: '/izvestaji', label: 'Izvestaji' },
]

export default function Layout({ children }) {
  const navigate = useNavigate()
  const rola = getRola()
  const isAdmin = rola === 'admin'
  const links = allLinks.filter(l => !l.adminOnly || isAdmin)

  function handleLogout() {
    clearAuth()
    navigate('/login')
  }

  return (
    <div className={styles.shell}>
      <aside className={styles.sidebar}>
        <div className={styles.brand}>Apoteka</div>
        <nav className={styles.nav}>
          {links.map(({ to, label, exact }) => (
            <NavLink
              key={to}
              to={to}
              end={exact}
              className={({ isActive }) =>
                `${styles.link} ${isActive ? styles.active : ''}`
              }
            >
              {label}
            </NavLink>
          ))}
        </nav>
        <div className={styles.bottom}>
          <span className={styles.rola}>{rola}</span>
          <button className={styles.logout} onClick={handleLogout}>Odjavi se</button>
        </div>
      </aside>
      <main className={styles.main}>{children}</main>
    </div>
  )
}
