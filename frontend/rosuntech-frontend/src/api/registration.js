import api from './axios';

/**
 * GET /api/registrations/myregist
 * – list kepemilikan registrasi mahasiswa
 */
export async function getMyRegistrations() {
  return api.get('/api/registrations/myregist');
}

/**
 * DELETE /api/registrations/{registrationId}
 * – batalkan / hapus registrasi mahasiswa
 */
export async function deleteRegistration(registrationId) {
  return api.delete(`/api/registrations/${registrationId}`);
}
