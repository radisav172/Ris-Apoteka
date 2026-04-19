import { getRola } from './auth'

export function useRole() {
  const rola = getRola()
  return {
    isAdmin: rola === 'admin',
    isKorisnik: rola === 'korisnik',
  }
}
