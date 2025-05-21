import api from './axios';

export async function createEvent(payload) {
  // payload = { title, description, date, time, maxParticipant, fotoPath }
  return api.post('/api/events', payload);
}
