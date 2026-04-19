import { useNavigate } from 'react-router-dom'
import { clearAuth } from '../auth'
import styles from './Navbar.module.css'

export default function Navbar() {
  const navigate = useNavigate()

  function handleLogout() {
    clearAuth()
    navigate('/login')
  }

  return (
    <header className={styles.navbar}>
      <span className={styles.logo}>Apoteka</span>
      <button className={styles.logout} onClick={handleLogout}>
        Odjavi se
      </button>
    </header>
  )
}
