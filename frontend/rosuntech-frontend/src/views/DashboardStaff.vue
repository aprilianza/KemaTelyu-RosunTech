 <template>
  <div class="d-flex">
    <!-- Sidebar -->
    <SidebarStaff />
    <!-- Main Content -->
    <div class="content-wrapper container py-5">
      <!-- User Info + Create Button -->
      <div class="user-profile-container mb-5">
        <div class="row g-4 align-items-center">
          <div class="col-12 col-md-4 text-center">
            <div class="profile-photo-container">
              <img :src="`http://localhost:8888/${user.photo}`" class="img-fluid rounded-circle profile-photo shadow" alt="User Photo" />
            </div>
          </div>
          <div class="col-12 col-md-8">
            <div class="d-flex flex-column flex-md-row justify-content-between align-items-md-center">
              <div class="user-info mb-3 mb-md-0">
                <h3 class="fw-bold mb-2">{{ user.name }}</h3>
                <div class="division-badge"><i class="bi bi-building me-2"></i>{{ user.division }}</div>
              </div>
              <router-link to="/CreateEvent" class="btn btn-lg create-event-btn"> <i class="bi bi-plus-circle me-2"></i>Create Event </router-link>
            </div>
          </div>
        </div>
      </div>

      <!-- Event List -->
      <div class="events-section">
        <div class="section-header d-flex justify-content-between align-items-center mb-4">
          <h5 class="section-title mb-0"><i class="bi bi-calendar-event me-2"></i>Current Events You Created</h5>
          <div class="event-counter badge bg-light text-primary">{{ events.length }} Events</div>
        </div>

        <div v-if="loadingEvents" class="text-center py-5">
          <div class="spinner-border text-primary" role="status">
            <span class="visually-hidden">Loading...</span>
          </div>
        </div>

        <div v-else class="row justify-content-center g-4 mb-5">
          <div class="col-12 col-md-6 col-lg-4" v-for="event in events" :key="event.id">
            <div class="event-card d-flex flex-column h-100">
              <div class="image-wrapper mb-3 position-relative">
                <img :src="getEventImage(event.fotoPath)" alt="Event Image" class="rounded-top" />
                <div class="event-date-overlay">
                  <span class="date">{{ formatDate(event.date).day }}</span>
                  <span class="month">{{ formatDate(event.date).month }}</span>
                </div>
              </div>
              <div class="card-body d-flex flex-column">
                <div class="badges-container mb-2">
                  <span class="badge time-badge me-2"> <i class="bi bi-clock me-1"></i>{{ formatTime(event.time) }} </span>
                  <span class="badge participant-badge"> <i class="bi bi-people me-1"></i>Max {{ event.maxParticipants || 50 }} </span>
                </div>
                <h5 class="event-title mb-2">{{ event.title }}</h5>
                <p class="event-description text-truncate mb-3">{{ event.description }}</p>
                <div class="mt-auto d-flex justify-content-between">
                  <button class="btn btn-outline-light btn-sm" @click="confirmDeleteEvent(event.id)"><i class="bi bi-trash me-1"></i> Delete Event</button>
                  <button class="btn btn-light btn-sm" @click="openModal(event)"><i class="bi bi-eye me-1"></i> Details</button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Modern Modal -->
      <div v-if="selectedEvent" class="modern-modal-container animate__animated animate__fadeIn">
        <div class="modern-modal-backdrop" @click="closeModal"></div>
        <div class="modern-modal animate__animated animate__zoomIn">
          <button type="button" class="modern-modal-close" @click="closeModal">
            <span class="close-icon">&times;</span>
          </button>

          <div class="modern-modal-image">
            <img :src="getEventImage(selectedEvent.fotoPath)" alt="Event Image" />
            <div class="event-date-badge">
              <div class="date-content">
                <span class="day">{{ formatDate(selectedEvent.date).day }}</span>
                <span class="month">{{ formatDate(selectedEvent.date).month }}</span>
                <span class="year">{{ formatDate(selectedEvent.date).year }}</span>
              </div>
            </div>
          </div>

          <div class="modern-modal-content">
            <h2 class="modal-event-title">{{ selectedEvent.title }}</h2>

            <div class="modal-event-metadata">
              <div class="metadata-item">
                <span class="metadata-icon calendar-icon"></span>
                <span>{{ formatFullDate(selectedEvent.date) }}</span>
              </div>
              <div class="metadata-item">
                <span class="metadata-icon clock-icon"></span>
                <span>{{ formatTime(selectedEvent.time) }}</span>
              </div>
              <div class="metadata-item">
                <span class="metadata-icon users-icon"></span>
                <span>Max {{ selectedEvent.maxParticipants || 50 }} Participants</span>
              </div>
            </div>

            <div class="modal-event-description">
              <p>{{ selectedEvent.description }}</p>
            </div>

            <!-- Participants Section -->
            <div class="participants-section mt-4">
              <h4 class="participants-title">
                <i class="bi bi-people-fill me-2"></i>Participants
                <span class="participant-count">({{ eventParticipants.length }})</span>
              </h4>

              <div class="table-responsive mt-3">
                <table class="table table-hover">
                  <thead>
                    <tr>
                      <th scope="col">#</th>
                      <th scope="col">Name</th>
                      <th scope="col">Email</th>
                      <th scope="col">Registration Date</th>
                      <th scope="col">Status</th>
                      <th scope="col">Actions</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="(participant, idx) in eventParticipants" :key="participant.id">
                      <td>{{ idx + 1 }}</td>
                      <td>{{ participant.user.name }}</td>
                      <td>{{ participant.user.email }}</td>
                      <td>{{ formatDateTime(participant.createdAt) }}</td>
                      <td>
                        <span :class="getStatusBadgeClass(participant.status)">
                          {{ participant.status }}
                        </span>
                      </td>
                      <td>
                        <div class="btn-group">
                          <button class="btn btn-sm btn-success" @click="updateParticipantStatus(participant.id, 'Accepted')" :disabled="participant.status === 'Accepted'">
                            <i class="bi bi-check-circle"></i>
                          </button>
                          <button class="btn btn-sm btn-danger" @click="updateParticipantStatus(participant.id, 'Rejected')" :disabled="participant.status === 'Rejected'">
                            <i class="bi bi-x-circle"></i>
                          </button>
                        </div>
                      </td>
                    </tr>
                    <tr v-if="eventParticipants.length === 0">
                      <td colspan="6" class="text-center py-3">No participants registered yet</td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>

            <div class="modern-modal-actions">
              <button class="btn btn-outline-danger" @click="closeModal"><i class="bi bi-x-circle me-2"></i>Close</button>
              <button class="btn btn-danger" @click="confirmDeleteEvent(selectedEvent.id)"><i class="bi bi-trash me-2"></i>Delete Event</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import SidebarStaff from '@/components/SidebarStaff.vue';
import axios from 'axios';
import Swal from 'sweetalert2';
import { getEvents, deleteEvent, getEventParticipants, updateParticipantStatus } from '@/api/event';

export default {
  name: 'DashboardStaff',
  components: { SidebarStaff },
  data() {
    return {
      isLoading: true,
      isAuthenticated: false,
      loadingEvents: false,
      user: {
        name: '',
        division: '',
        photo: 'profile.png',
      },
      events: [],
      selectedEvent: null,
      eventParticipants: [],
    };
  },
  async mounted() {
    await this.checkAuthentication();
    await this.fetchEvents();
  },
  methods: {
    async checkAuthentication() {
      try {
        const token = localStorage.getItem('token');
        const userData = localStorage.getItem('user');
        
        if (!token) {
          await this.showAuthError('Autentikasi Diperlukan', 'Silakan login untuk mengakses halaman ini');
          this.redirectToLogin();
          return;
        }

        let user = null;
        if (userData) {
          try {
            user = JSON.parse(userData);
          } catch (error) {
            console.error('Error parsing user data:', error);
            await this.showAuthError('Data User Tidak Valid', 'Data user tidak valid. Silakan login kembali');
            this.redirectToLogin();
            return;
          }
        }

        if (!user || !user.role || user.role.toUpperCase() !== 'STAFF') {
          await this.showAuthError('Akses Ditolak', 'Anda memerlukan hak akses staff untuk membuka halaman ini');
          this.redirectToLogin();
          return;
        }

        const response = await axios.get('/api/auth/me', {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.status === 200) {
          this.user = {
            ...this.user,
            ...response.data,
            name: response.data.name || this.user.name,
            division: response.data.divisi || this.user.divisi,
            photo: response.data.fotoPath || this.user.fotoPath,
          };

          this.isAuthenticated = true;
          this.isLoading = false;
        } else {
          await this.showAuthError('Autentikasi Gagal', 'Gagal memverifikasi kredensial Anda');
          this.redirectToLogin();
        }
      } catch (error) {
        console.error('Authentication failed:', error);

        if (error.response) {
          if (error.response.status === 401) {
            await this.showAuthError('Sesi Habis', 'Sesi login Anda telah habis. Silakan login kembali');
          } else if (error.response.status === 403) {
            await this.showAuthError('Akses Ditolak', 'Anda tidak memiliki izin untuk mengakses halaman ini');
          } else {
            await this.showAuthError('Kesalahan Server', 'Terjadi kesalahan pada server. Silakan coba lagi nanti');
          }
        } else if (error.request) {
          await this.showAuthError('Koneksi Gagal', 'Tidak dapat terhubung ke server. Periksa koneksi internet Anda');
        } else {
          await this.showAuthError('Kesalahan', 'Terjadi kesalahan saat memverifikasi autentikasi');
        }
        this.clearAuthData();
        this.redirectToLogin();
      }
    },

    async fetchEvents() {
      this.loadingEvents = true;
      try {
        const response = await getEvents();
        this.events = response.data;
      } catch (error) {
        console.error('Error fetching events:', error);
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'Failed to load events. Please try again later.',
        });
      } finally {
        this.loadingEvents = false;
      }
    },

    async fetchEventParticipants(eventId) {
      try {
        const response = await getEventParticipants(eventId);
        this.eventParticipants = response.data;
      } catch (error) {
        console.error('Error fetching participants:', error);
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'Failed to load participants. Please try again later.',
        });
      }
    },

    async showAuthError(title, message) {
      await Swal.fire({
        icon: 'error',
        title: title,
        text: message,
        confirmButtonText: 'Ke Halaman Login',
        allowOutsideClick: false,
        customClass: {
          confirmButton: 'btn btn-danger',
        },
      });
    },

    clearAuthData() {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
    },

    redirectToLogin() {
      this.isLoading = false;
      this.isAuthenticated = false;
      this.$router.push('/');
    },

    async openModal(event) {
      this.selectedEvent = event;
      document.body.classList.add('modal-open');
      await this.fetchEventParticipants(event.id);
    },

    closeModal() {
      this.selectedEvent = null;
      document.body.classList.remove('modal-open');
    },

    getEventImage(fotoPath) {
      if (!fotoPath) return require('@/assets/img/placeholder.jpg');
      return `http://localhost:8888/${fotoPath}`;
    },

    formatDate(dateString) {
      const date = new Date(dateString);
      const optionsDay = { day: 'numeric' };
      const optionsMonth = { month: 'long' };
      const optionsYear = { year: 'numeric' };
      
      return {
        day: date.toLocaleDateString('id-ID', optionsDay),
        month: date.toLocaleDateString('id-ID', optionsMonth),
        year: date.toLocaleDateString('id-ID', optionsYear)
      };
    },

    formatFullDate(dateString) {
      const date = new Date(dateString);
      const options = { day: 'numeric', month: 'long', year: 'numeric' };
      return date.toLocaleDateString('id-ID', options);
    },

    formatTime(timeString) {
      if (!timeString) return '';
      return timeString.substring(0, 5);
    },

    formatDateTime(dateTimeString) {
      if (!dateTimeString) return '';
      const date = new Date(dateTimeString);
      return date.toLocaleString('id-ID');
    },

    async confirmDeleteEvent(eventId) {
      const result = await Swal.fire({
        title: 'Hapus Event',
        text: 'Apakah Anda yakin ingin menghapus event ini? Tindakan ini tidak dapat dibatalkan.',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        cancelButtonColor: '#3085d6',
        confirmButtonText: 'Ya, hapus!',
        cancelButtonText: 'Batal',
      });

      if (result.isConfirmed) {
        try {
          await deleteEvent(eventId);
          this.events = this.events.filter(event => event.id !== eventId);
          
          if (this.selectedEvent && this.selectedEvent.id === eventId) {
            this.closeModal();
          }

          const Toast = Swal.mixin({
            toast: true,
            position: 'top-end',
            showConfirmButton: false,
            timer: 3000,
            timerProgressBar: true,
          });

          Toast.fire({
            icon: 'success',
            title: 'Event berhasil dihapus',
          });
        } catch (error) {
          console.error('Error deleting event:', error);
          Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'Failed to delete event. Please try again later.',
          });
        }
      }
    },

    async updateParticipantStatus(registrationId, newStatus) {
      try {
        await updateParticipantStatus(registrationId, newStatus);
        
        // Update local state
        const participant = this.eventParticipants.find(p => p.id === registrationId);
        if (participant) {
          participant.status = newStatus;
        }

        const Toast = Swal.mixin({
          toast: true,
          position: 'top-end',
          showConfirmButton: false,
          timer: 3000,
          timerProgressBar: true,
        });

        Toast.fire({
          icon: 'success',
          title: `Status diperbarui ke ${newStatus}`,
        });
      } catch (error) {
        console.error('Error updating participant status:', error);
        Swal.fire({
          icon: 'error',
          title: 'Error',
          text: 'Failed to update participant status. Please try again later.',
        });
      }
    },

    getStatusBadgeClass(status) {
      switch (status) {
        case 'Accepted':
          return 'badge bg-success';
        case 'Processing':
          return 'badge bg-secondary';
        case 'Rejected':
          return 'badge bg-danger';
        default:
          return 'badge bg-secondary';
      }
    },
  },

  beforeRouteEnter(to, from, next) {
    const token = localStorage.getItem('token');
    const userData = localStorage.getItem('user');

    if (!token) {
      next('/');
      return;
    }

    if (userData) {
      try {
        const user = JSON.parse(userData);
        if (!user.role || user.role.toUpperCase() !== 'STAFF') {
          next('/');
          return;
        }
      } catch (error) {
        next('/');
        return;
      }
    }

    next();
  },
};
</script>
<style scoped>
.content-wrapper {
  flex-grow: 1;
  margin-top: 80px;
}

/* User Profile */
.profile-photo-container {
  display: inline-block;
  padding: 4px;
  border-radius: 50%;
  background-image: linear-gradient(45deg, #b30202, #dd3232);
}

.profile-photo {
  width: 180px;
  height: 180px;
  object-fit: cover;
  border: 4px solid white;
}

.user-info h3 {
  color: #212121;
  font-size: 1.75rem;
}

.division-badge {
  display: inline-block;
  background-color: rgba(179, 2, 2, 0.1);
  color: #b30202;
  padding: 0.5rem 1rem;
  border-radius: 2rem;
  font-weight: 500;
}

.create-event-btn {
  border-radius: 50px;
  padding: 0.75rem 1.5rem;
  font-weight: 600;
  background-color: #b30202;
  color: #fff1f1;
  box-shadow: 0 4px 10px rgba(179, 2, 2, 0.3);
  transition: all 0.3s ease;
  border: none;
}

.create-event-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 12px rgba(179, 2, 2, 0.4);
  background-color: #dd3232;
  color: white;
}

/* Section Header */
.section-header {
  border-bottom: 2px solid rgba(179, 2, 2, 0.2);
  padding-bottom: 0.75rem;
}

.section-title {
  color: #b30202;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  position: relative;
  padding-left: 0.5rem;
}

.section-title::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  height: 100%;
  width: 4px;
  background-color: #b30202;
  border-radius: 4px;
}

.event-counter {
  font-size: 0.85rem;
  padding: 0.4rem 0.8rem;
  color: #b30202 !important;
}

/* Event Cards */
.event-card {
  background: linear-gradient(135deg, #2c3e50, #1a252f);
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s ease;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.15);
}

.event-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.2);
}

.image-wrapper {
  height: 180px;
  overflow: hidden;
}

.image-wrapper img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.5s ease;
}

.event-card:hover .image-wrapper img {
  transform: scale(1.05);
}

.event-date-overlay {
  position: absolute;
  top: 15px;
  right: 15px;
  background-color: rgba(179, 2, 2, 0.9);
  color: white;
  border-radius: 8px;
  padding: 0.5rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  min-width: 50px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
}

.event-date-overlay .date {
  font-size: 1.2rem;
  font-weight: 700;
  line-height: 1;
}

.event-date-overlay .month {
  font-size: 0.8rem;
  text-transform: uppercase;
}

.card-body {
  padding: 1.5rem;
}

.badges-container {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.time-badge,
.participant-badge {
  background-color: rgba(255, 255, 255, 0.15);
  color: rgba(255, 255, 255, 0.9);
  border-radius: 50px;
  padding: 0.35rem 0.75rem;
  font-size: 0.75rem;
  font-weight: 500;
}

.event-title {
  color: white;
  font-size: 1.1rem;
  font-weight: 600;
  line-height: 1.4;
  max-height: 3.1em;
  overflow: hidden;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.event-description {
  color: rgba(255, 255, 255, 0.7);
  font-size: 0.9rem;
}

.btn-outline-light {
  border-color: rgba(255, 255, 255, 0.3);
  color: rgba(255, 255, 255, 0.9);
}

.btn-outline-light:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

/* Modern Modal Styles */
.modern-modal-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1050;
}

.modern-modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.7);
  backdrop-filter: blur(5px);
  z-index: 1051;
}

.modern-modal {
  background-color: white;
  width: 90%;
  max-width: 800px;
  border-radius: 1.5rem;
  overflow: hidden;
  z-index: 1052;
  position: relative;
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.2);
  max-height: 90vh;
  overflow-y: auto;
}

/* Enhanced Close Button */
.modern-modal-close {
  position: absolute;
  top: 15px;
  right: 15px;
  background-color: rgba(0, 0, 0, 0.6);
  border: 2px solid white;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  z-index: 1060;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.3);
  transition: all 0.3s ease;
}

.modern-modal-close:hover {
  background-color: rgba(0, 0, 0, 0.8);
  transform: scale(1.1);
}

.close-icon {
  color: white;
  font-size: 24px;
  line-height: 1;
  font-weight: bold;
}

.modern-modal-image {
  width: 100%;
  height: auto;
  overflow: hidden;
  position: relative;
}

.modern-modal-image img {
  width: 100%;
  height: auto;
}

.event-date-badge {
  position: absolute;
  top: 20px;
  right: 20px;
  background-color: white;
  border-radius: 10px;
  padding: 0.75rem 1rem;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.date-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  line-height: 1.2;
}

.date-content .day {
  font-size: 1.5rem;
  font-weight: 700;
  color: #b30202;
}

.date-content .month {
  font-size: 0.9rem;
  font-weight: 600;
  text-transform: uppercase;
  color: #495057;
}

.date-content .year {
  font-size: 0.8rem;
  color: #868686;
}

.modern-modal-content {
  padding: 2rem;
}

.modal-event-title {
  font-size: 1.8rem;
  font-weight: 700;
  margin-bottom: 1.5rem;
  color: #333;
}

.modal-event-metadata {
  display: flex;
  flex-wrap: wrap;
  gap: 1.5rem;
  margin-bottom: 1.5rem;
  color: #868686;
}

.metadata-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

/* Custom icons instead of FontAwesome */
.metadata-icon {
  display: inline-block;
  width: 20px;
  height: 20px;
  background-position: center;
  background-repeat: no-repeat;
  background-size: contain;
}

.calendar-icon {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' fill='currentColor' class='bi bi-calendar' viewBox='0 0 16 16'%3E%3Cpath d='M3.5 0a.5.5 0 0 1 .5.5V1h8V.5a.5.5 0 0 1 1 0V1h1a2 2 0 0 1 2 2v11a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V3a2 2 0 0 1 2-2h1V.5a.5.5 0 0 1 .5-.5zM1 4v10a1 1 0 0 0 1 1h12a1 1 0 0 0 1-1V4H1z'/%3E%3C/svg%3E");
}

.clock-icon {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' fill='currentColor' class='bi bi-clock' viewBox='0 0 16 16'%3E%3Cpath d='M8 3.5a.5.5 0 0 0-1 0V9a.5.5 0 0 0 .252.434l3.5 2a.5.5 0 0 0 .496-.868L8 8.71V3.5z'/%3E%3Cpath d='M8 16A8 8 0 1 0 8 0a8 8 0 0 0 0 16zm7-8A7 7 0 1 1 1 8a7 7 0 0 1 14 0z'/%3E%3C/svg%3E");
}

.users-icon {
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' fill='currentColor' class='bi bi-people' viewBox='0 0 16 16'%3E%3Cpath d='M15 14s1 0 1-1-1-4-5-4-5 3-5 4 1 1 1 1h8zm-7.978-1A.261.261 0 0 1 7 12.996c.001-.264.167-1.03.76-1.72C8.312 10.629 9.282 10 11 10c1.717 0 2.687.63 3.24 1.276.593.69.758 1.457.76 1.72l-.008.002a.274.274 0 0 1-.014.002H7.022zM11 7a2 2 0 1 0 0-4 2 2 0 0 0 0 4zm3-2a3 3 0 1 1-6 0 3 3 0 0 1 6 0zM6.936 9.28a5.88 5.88 0 0 0-1.23-.247A7.35 7.35 0 0 0 5 9c-4 0-5 3-5 4 0 .667.333 1 1 1h4.216A2.238 2.238 0 0 1 5 13c0-1.01.377-2.042 1.09-2.904.243-.294.526-.569.846-.816zM4.92 10A5.493 5.493 0 0 0 4 13H1c0-.26.164-1.03.76-1.724.545-.636 1.492-1.256 3.16-1.275zM1.5 5.5a3 3 0 1 1 6 0 3 3 0 0 1-6 0zm3-2a2 2 0 1 0 0 4 2 2 0 0 0 0-4z'/%3E%3C/svg%3E");
}

.modal-event-description {
  margin-bottom: 1rem;
  line-height: 1.6;
  color: #555;
}

/* Participants Section */
.participants-section {
  background-color: #f8f9fa;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.participants-title {
  color: #333;
  font-size: 1.25rem;
  font-weight: 600;
  display: flex;
  align-items: center;
  margin-bottom: 1rem;
}

.participant-count {
  font-size: 0.9rem;
  color: #868686;
  margin-left: 0.5rem;
}

.table th {
  font-weight: 600;
  color: #495057;
  background-color: rgba(179, 2, 2, 0.05);
}

.table td {
  vertical-align: middle;
}

.btn-group .btn {
  border-radius: 4px;
  padding: 0.25rem 0.5rem;
  margin: 0 0.1rem;
}

.modern-modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 2rem;
}

.btn-outline-danger {
  color: #b30202;
  border-color: #b30202;
}

.btn-outline-danger:hover {
  background-color: #b30202;
  color: white;
}

.btn-danger {
  background-color: #b30202;
  border-color: #b30202;
}

.btn-danger:hover {
  background-color: #dd3232;
  border-color: #dd3232;
}

/* Animation delays for staggered effect */
.animate__fadeIn {
  --animate-duration: 0.6s;
}

.animate__zoomIn {
  --animate-duration: 0.5s;
}

body.modal-open {
  overflow: hidden;
}
</style>