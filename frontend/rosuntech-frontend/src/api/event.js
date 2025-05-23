import api from './axios';

export async function createEvent(payload) {
  return api.post('/api/events', payload);
}

export async function getEvents() {
  return api.get('/api/events');
}

export async function deleteEvent(id) {
  return api.delete(`/api/events/${id}`);
}

export async function getEventParticipants(eventId) {
  return api.get(`/api/events/${eventId}/participants`);
}

export async function updateParticipantStatus(registrationId, status) {
  const endpoint = status === 'Accepted' 
    ? `approve` 
    : status === 'Rejected' 
      ? `reject` 
      : '';
  
  if (!endpoint) return Promise.reject('Invalid status');
  
  return api.put(`/api/events/participants/${registrationId}/${endpoint}`);
}