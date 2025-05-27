import api from './axios';

// Ambil data registrasi milik user yang login
export async function getMyRegistrations() {
  return api.get('/api/registrations/myregist');
}