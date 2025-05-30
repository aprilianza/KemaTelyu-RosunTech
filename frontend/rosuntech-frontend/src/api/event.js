import api from './axios';

export async function createEvent(payload) {
  return api.post('/api/events', payload);
}

export async function getEvents() {
  return api.get('/api/events');
}

export async function getEventsStaff() {
  return api.get('/api/events/staff/myevents');
}

export async function deleteEvent(id) {
  return api.delete(`/api/events/${id}`);
}

export async function getEventParticipants(eventId) {
  return api.get(`/api/events/${eventId}/participants`);
}

export async function updateParticipantStatus(registrationId, status) {
  if (!['APPROVED', 'REJECTED'].includes(status)) {
    throw new Error('Status must be "APPROVED" or "REJECTED"');
  }
  return api.patch(`/api/events/participants/${registrationId}/${status}`);
}

export async function updateEvent(id, payload) {
  return api.put(`/api/events/${id}`, payload);

}
