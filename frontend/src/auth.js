export function saveAuth(token, rola) {
  localStorage.setItem('token', token)
  localStorage.setItem('rola', rola)
}

export function clearAuth() {
  localStorage.removeItem('token')
  localStorage.removeItem('rola')
}

export function isLoggedIn() {
  return !!localStorage.getItem('token')
}

export function getRola() {
  return localStorage.getItem('rola')
}
