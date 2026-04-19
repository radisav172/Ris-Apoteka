import { Routes, Route, Navigate } from 'react-router-dom'
import { isLoggedIn } from './auth'
import Layout from './components/Layout'
import Dashboard from './pages/Dashboard'
import LoginPage from './pages/LoginPage'
import RegisterPage from './pages/RegisterPage'
import LekPage from './pages/LekPage'
import PacijentPage from './pages/PacijentPage'
import ProdajaPage from './pages/ProdajaPage'
import NabavkaPage from './pages/NabavkaPage'
import ReceptPage from './pages/ReceptPage'
import DobavljacPage from './pages/DobavljacPage'
import KategorijePage from './pages/KategorijePage'
import KorisnikPage from './pages/KorisnikPage'
import IzvestajiPage from './pages/IzvestajiPage'

function PrivateRoute({ children }) {
  if (!isLoggedIn()) return <Navigate to="/login" replace />
  return <Layout>{children}</Layout>
}

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/" element={<PrivateRoute><Dashboard /></PrivateRoute>} />
      <Route path="/lekovi" element={<PrivateRoute><LekPage /></PrivateRoute>} />
      <Route path="/pacijenti" element={<PrivateRoute><PacijentPage /></PrivateRoute>} />
      <Route path="/prodaje" element={<PrivateRoute><ProdajaPage /></PrivateRoute>} />
      <Route path="/nabavke" element={<PrivateRoute><NabavkaPage /></PrivateRoute>} />
      <Route path="/recepti" element={<PrivateRoute><ReceptPage /></PrivateRoute>} />
      <Route path="/dobavljaci" element={<PrivateRoute><DobavljacPage /></PrivateRoute>} />
      <Route path="/kategorije" element={<PrivateRoute><KategorijePage /></PrivateRoute>} />
      <Route path="/korisnici" element={<PrivateRoute><KorisnikPage /></PrivateRoute>} />
      <Route path="/izvestaji" element={<PrivateRoute><IzvestajiPage /></PrivateRoute>} />
      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  )
}
